package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User

data class UserWithGroups(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "groupId",
        associateBy = Junction(GroupMember::class)
    )
    val groups: List<Group>
)