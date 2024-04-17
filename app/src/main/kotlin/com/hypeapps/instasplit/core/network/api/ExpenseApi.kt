package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Expense
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseApi {


    // Fetch all expenses
    @GET("api/expenses")
    suspend fun getExpenses(): List<Expense>

    // add expense
    @POST("api/expenses")
    suspend fun addExpenses(): Expense

    // Delete an expense by ID
    @DELETE("api/expenses/{expense_id}")
    suspend fun deleteExpense(@Path("expense_id") expenseId: Int): Unit
}