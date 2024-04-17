package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.utils.LoginRequest
import com.hypeapps.instasplit.core.utils.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("api/users")
    suspend fun getUsers(): List<User>


    @POST("api/users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<User>

    @POST("api/users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<User>
}