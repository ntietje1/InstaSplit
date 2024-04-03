package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWithUsers
import com.hypeapps.instasplit.core.model.entity.Group

@Dao
interface GroupDao {

    @Query("SELECT * FROM 'group' WHERE groupId = :groupId LIMIT 1")
    suspend fun getGroupById(groupId: Int): Group

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Query("DELETE FROM 'group' WHERE groupId = :groupId")
    suspend fun deleteGroupById(groupId: Int)

    @Transaction
    @Query("SELECT * FROM 'group' WHERE groupId = :groupId LIMIT 1")
    suspend fun getGroupWithUsers(groupId: Int): GroupWithUsers

}