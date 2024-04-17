package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.ImageResponse
import retrofit2.http.GET


interface ImageApi {

    @GET("api/random_member_image")
    fun getRandomMemberImage(): ImageResponse

    @GET("api/random_group_image")
    fun getRandomGroupImage(): ImageResponse

    @GET("api/random_expense_image")
    fun getRandomExpenseImage(): ImageResponse
}

