package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("user/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): User

    @POST("user")
    suspend fun addUser(@Body user: User)

    @PUT("user")
    suspend fun updateUser(@Body user: User)

    @DELETE("user/{userId}")
    suspend fun deleteUserById(@Path("userId") userId: Int)

    @GET("user/{userId}/info")
    suspend fun getUserWrapper(@Path("userId") userId: Int): UserWrapper
}