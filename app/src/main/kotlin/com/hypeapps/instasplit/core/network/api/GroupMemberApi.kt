package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.GroupMember
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupMemberApi {

    // Add a new member to a specific group
    @POST("api/groups/{groupName}/members")
    suspend fun insertMember(@Path("groupName") groupName: String, @Body groupMember: GroupMember)

    // Delete a member from a specific group by email (assuming email is used as a unique identifier)
    @DELETE("api/groups/{groupName}/members/{memberEmail}")
    suspend fun deleteMember(@Path("groupName") groupName: String, @Path("memberEmail") memberEmail: String)
}