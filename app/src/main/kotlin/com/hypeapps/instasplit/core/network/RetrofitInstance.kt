package com.hypeapps.instasplit.core.network


import com.hypeapps.instasplit.core.network.api.ExpenseApi
import com.hypeapps.instasplit.core.network.api.GroupApi
import com.hypeapps.instasplit.core.network.api.GroupMemberApi
import com.hypeapps.instasplit.core.network.api.ImageApi
import com.hypeapps.instasplit.core.network.api.UserApi
import com.hypeapps.instasplit.core.network.api.UserExpenseApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL: String = "http://ngokho.pythonanywhere.com/"

    val instaSplitApi: InstaSplitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstaSplitApi::class.java)
    }
}

interface InstaSplitApi: ExpenseApi, GroupApi, GroupMemberApi, UserApi, UserExpenseApi, ImageApi {
    // Additional endpoints can be defined here if needed
}