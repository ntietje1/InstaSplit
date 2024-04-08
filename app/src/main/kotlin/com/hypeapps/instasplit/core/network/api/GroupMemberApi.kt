package com.hypeapps.instasplit.core.network.api

import com.hypeapps.instasplit.core.model.entity.GroupMember
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface GroupMemberApi {

    @POST("groupMember")
    suspend fun insert(@Body groupMember: GroupMember)

    @DELETE("groupMember")
    suspend fun delete(@Body groupMember: GroupMember)
}