package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.UserExpense
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface UserExpenseApi {

    @POST("userExpense")
    suspend fun insert(@Body userExpense: UserExpense)

    @DELETE("userExpense")
    suspend fun delete(@Body userExpense: UserExpense)
}