package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.UserExpense
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface UserExpenseApi {

    @GET("api/user_expenses")
    suspend fun getUserExpenses(): List<UserExpense>


    @POST("api/user_expenses")
    suspend fun addUserExpenses(@Body userExpense: UserExpense): UserExpense



}