package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseApi {
    @GET("expense/{expenseId}")
    suspend fun getExpenseById(@Path("expenseId") expenseId: Int): Expense

    @GET("expense/group/{groupId}")
    suspend fun getExpensesByGroupId(@Path("groupId") groupId: Int): List<Expense>

    @POST("expense")
    suspend fun addExpense(@Body expense: Expense)

    @PUT("expense")
    suspend fun updateExpense(@Body expense: Expense)

    @DELETE("expense/{expenseId}")
    suspend fun deleteExpenseById(@Path("expenseId") expenseId: Int)

    @GET("expense/users/{expenseId}")
    suspend fun getExpenseWrapper(@Path("expenseId") expenseId: Int): ExpenseWrapper
}