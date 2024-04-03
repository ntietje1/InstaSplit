package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.hypeapps.instasplit.core.model.entity.GroupMember

@Dao
interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupMember: GroupMember)

    @Delete
    suspend fun delete(groupMember: GroupMember)
}