package com.mhl.practice.model

import com.mhl.practice.utils.Users
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersService {
    @GET("auth")
    suspend fun authenticate(
        @Query("email") email: String,
        @Query("password") password: String
    ): Users

    @POST("createUser")
    suspend fun createUser(
        @Body users: Users
    ): ResponseBody
}