package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User

data class GroupWrapper(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "userId",
        associateBy = Junction(GroupMember::class)
    )
    val users: List<User>,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId",
    )
    val expenses: List<Expense>
) {

}