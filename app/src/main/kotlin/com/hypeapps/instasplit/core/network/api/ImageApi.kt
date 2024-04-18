package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.network.ImageResponse
import retrofit2.http.GET


interface ImageApi {

    @GET("api/random_member_image")
    suspend fun getRandomMemberImage(): ImageResponse

    @GET("api/random_group_image")
    suspend fun getRandomGroupImage(): ImageResponse

    @GET("api/random_expense_image")
    suspend fun getRandomExpenseImage(): ImageResponse
}

