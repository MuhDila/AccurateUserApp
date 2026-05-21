package com.muhdila.accurateuserapp.user.data.remote

import com.muhdila.accurateuserapp.user.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("user")
    suspend fun getUsers(): Response<List<UserDto>>

    @POST("user")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>
}
