package com.hypeapps.instasplit.core.network


import com.hypeapps.instasplit.core.network.InstaSplitApi.Companion.BASE_URL
import com.hypeapps.instasplit.core.network.api.ExpenseApi
import com.hypeapps.instasplit.core.network.api.GroupApi
import com.hypeapps.instasplit.core.network.api.GroupMemberApi
import com.hypeapps.instasplit.core.network.api.UserApi
import com.hypeapps.instasplit.core.network.api.UserExpenseApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitInstance {
    val instaSplitApi: InstaSplitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstaSplitApi::class.java)
    }
}

interface InstaSplitApi: ExpenseApi, GroupApi, GroupMemberApi, UserApi, UserExpenseApi {
    companion object {
        const val BASE_URL: String = "http://ngokho.pythonanywhere.com/"
    }

    @GET("api/expenses/image")
    suspend fun getExpenseImage(): String

    @GET("api/groups/image")
    suspend fun getGroupImage(): String

    @GET("api/users/image")
    suspend fun getUserImage(): String

}