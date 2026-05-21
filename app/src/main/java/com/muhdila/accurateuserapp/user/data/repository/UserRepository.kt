package com.muhdila.accurateuserapp.user.data.repository

import com.muhdila.accurateuserapp.core.data.safeApiCall
import com.muhdila.accurateuserapp.core.data.safeDbCall
import com.muhdila.accurateuserapp.core.domain.DataError
import com.muhdila.accurateuserapp.core.domain.Result
import com.muhdila.accurateuserapp.user.data.local.dao.UserDao
import com.muhdila.accurateuserapp.user.data.mapper.toDomain
import com.muhdila.accurateuserapp.user.data.mapper.toDto
import com.muhdila.accurateuserapp.user.data.mapper.toEntity
import com.muhdila.accurateuserapp.user.data.remote.UserApiService
import com.muhdila.accurateuserapp.user.domain.model.User
import com.muhdila.accurateuserapp.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService
) : IUserRepository {

    override fun getUsers(): Flow<List<User>> =
        userDao.getAllUsers().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun addUser(user: User): Result<User, DataError.Remote> {
        
        val localUser = user.copy(
            id = UUID.randomUUID().toString(),
            isSynced = false
        )

        safeDbCall { userDao.upsertUser(localUser.toEntity()) }

        return when (val result = safeApiCall { userApiService.createUser(localUser.toDto()) }) {
            is Result.Success -> {
                val serverUser = result.data.toDomain(isSynced = true)
                safeDbCall {
                    userDao.deleteUser(localUser.id)
                    userDao.upsertUser(serverUser.toEntity())
                }
                Result.Success(serverUser)
            }
            is Result.Error -> {
                val error = result.error
                if (error is DataError.Remote.NoInternet || error is DataError.Remote.RequestTimeout) {
                    Result.Success(localUser)
                } else {
                    safeDbCall { userDao.deleteUser(localUser.id) }
                    Result.Error(error)
                }
            }
        }
    }

    override suspend fun syncUsers(): Result<Unit, DataError.Remote> {
        
        val unsyncedUsers = userDao.getUnsyncedUsers()
        for (entity in unsyncedUsers) {
            val result = safeApiCall {
                userApiService.createUser(entity.toDomain().toDto())
            }
            when (result) {
                is Result.Success -> {
                    userDao.markAsSynced(entity.id)
                }
                is Result.Error -> {
                    val error = result.error
                    if (error !is DataError.Remote.NoInternet && error !is DataError.Remote.RequestTimeout) {
                        safeDbCall { userDao.deleteUser(entity.id) }
                    }
                }
            }
        }

        return when (val result = safeApiCall { userApiService.getUsers() }) {
            is Result.Success -> {
                val entities = result.data.map { it.toEntity(isSynced = true) }
                safeDbCall { userDao.upsertUsers(entities) }
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }
}
