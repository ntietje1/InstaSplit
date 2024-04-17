package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hypeapps.instasplit.core.model.entity.GroupMember

@Dao
interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupMember: GroupMember)

    @Delete
    suspend fun delete(groupMember: GroupMember)

    @Query("SELECT * FROM groupmember WHERE groupId = :groupId")
    suspend fun getGroupMembers(groupId: Int): List<GroupMember>
}