package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper

@Dao
interface GroupDao {

    @Query("SELECT * FROM 'group'")
    suspend fun getGroups(): List<Group>

    @Transaction
    @Query("SELECT * FROM `Group` WHERE groupId = :groupId")
    suspend fun getGroupWithUsersAndExpenses(groupId: Int): GroupWrapper

    @Query("SELECT * FROM 'group' WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: Int): Group

    @Insert(onConflict = REPLACE)
    suspend fun addGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Query("DELETE FROM 'group' WHERE groupId = :groupId")
    suspend fun deleteGroupById(groupId: Int)


}