package com.muhdila.accurateuserapp.user.domain.repository

import com.muhdila.accurateuserapp.core.domain.DataError
import com.muhdila.accurateuserapp.core.domain.Result
import com.muhdila.accurateuserapp.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun addUser(user: User): Result<User, DataError.Remote>
    suspend fun syncUsers(): Result<Unit, DataError.Remote>
}
