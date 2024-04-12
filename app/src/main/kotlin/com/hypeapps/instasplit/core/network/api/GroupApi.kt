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

    @GET("group")
    suspend fun getGroups(): List<Group>

    @GET("group/{groupId}")
    suspend fun getGroupById(@Path("groupId") groupId: Int): Group

    @GET("group/{groupId}/info")
    suspend fun getGroupWrapper(groupId: Int): GroupWrapper

    @POST("group")
    suspend fun addGroup(@Body group: Group)

    @PUT("group")
    suspend fun updateGroup(@Body group: Group)

    @DELETE("group/{groupId}")
    suspend fun deleteGroupById(@Path("groupId") groupId: Int)
}