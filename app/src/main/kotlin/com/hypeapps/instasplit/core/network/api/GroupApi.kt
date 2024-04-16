package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
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

    // Fetch a specific group by name (not by ID, as your Flask uses group names as keys)
    @GET("api/groups/{groupName}")
    suspend fun getGroupByName(@Path("groupName") groupName: String): Group

    // Add a new group
    @POST("api/groups")
    suspend fun addGroup(@Body group: Group)

    // Update an existing group by name
    @PUT("api/groups/{groupName}")
    suspend fun updateGroup(@Path("groupName") groupName: String, @Body group: Group)

    // Delete a group by name
    @DELETE("api/groups/{groupName}")
    suspend fun deleteGroupByName(@Path("groupName") groupName: String)
}
