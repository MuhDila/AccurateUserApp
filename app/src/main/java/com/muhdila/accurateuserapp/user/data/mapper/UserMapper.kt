package com.muhdila.accurateuserapp.user.data.mapper

import com.muhdila.accurateuserapp.user.data.local.entity.UserEntity
import com.muhdila.accurateuserapp.user.data.model.UserDto
import com.muhdila.accurateuserapp.user.domain.model.Gender
import com.muhdila.accurateuserapp.user.domain.model.User

fun UserDto.toEntity(isSynced: Boolean = true): UserEntity = UserEntity(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender,
    isSynced = isSynced
)

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = Gender.fromValue(gender),
    isSynced = isSynced
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender.value,
    isSynced = isSynced
)

fun User.toDto(): UserDto = UserDto(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender.value
)

fun UserDto.toDomain(isSynced: Boolean = true): User = User(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = Gender.fromValue(gender),
    isSynced = isSynced
)
