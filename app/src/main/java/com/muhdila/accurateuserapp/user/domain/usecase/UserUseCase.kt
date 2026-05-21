package com.muhdila.accurateuserapp.user.domain.usecase

import com.muhdila.accurateuserapp.core.domain.DataError
import com.muhdila.accurateuserapp.core.domain.Result
import com.muhdila.accurateuserapp.user.domain.model.User
import com.muhdila.accurateuserapp.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    operator fun invoke(): Flow<List<User>> = userRepository.getUsers()
}

class AddUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(user: User): Result<User, DataError.Remote> =
        userRepository.addUser(user)
}

class SyncUsersUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(): Result<Unit, DataError.Remote> =
        userRepository.syncUsers()
}
