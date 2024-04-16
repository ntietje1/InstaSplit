package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Expense
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseApi {

    // Fetch all expenses for a specific group
    @GET("api/groups/{groupName}/expenses")
    suspend fun getExpensesByGroupName(@Path("groupName") groupName: String): List<Expense>

    // Add a new expense to a specific group
    @POST("api/groups/{groupName}/expenses")
    suspend fun addExpense(@Path("groupName") groupName: String, @Body expense: Expense)

    // Delete an expense by its "expenseId". Note: Need to decide on the ID.
    @DELETE("api/groups/{groupName}/expenses/{expenseId}")
    suspend fun deleteExpenseById(
        @Path("groupName") groupName: String,
        @Path("expenseId") expenseId: String
    )
}
