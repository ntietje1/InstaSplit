package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Group
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApi {

    // Fetch all groups
    @GET("api/groups")
    suspend fun getGroups(): List<Group>


    // add group
    @POST("api/groups")
    suspend fun addGroups(): Group


}
