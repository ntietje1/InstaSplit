package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupMemberApi {

    @GET("api/group_members")
    suspend fun getGroupMembers(): List<GroupMember>

    @POST("api/groups/{group_id}/members")
    suspend fun addGroupMembers(@Path("group_id") groupId: Int, @Body groupMember: GroupMember): GroupMember
    @DELETE("api/groups/{group_id}/members/{user_id}")
    suspend fun deleteGroupMember(
        @Path("group_id") groupId: Int,
        @Path("user_id") userId: Int
    ): Unit

}