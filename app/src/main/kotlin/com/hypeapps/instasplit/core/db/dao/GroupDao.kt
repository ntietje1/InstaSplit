package com.hypeapps.instasplit.core.db.dao

import androidx.lifecycle.LiveData
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

    @Transaction
    @Query("SELECT * FROM `group` WHERE groupId = :groupId")
    fun getGroupWrapperLiveData(groupId: Int): LiveData<GroupWrapper>

    @Query("SELECT * FROM 'group' WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: Int): Group

    @Insert(onConflict = REPLACE)
    suspend fun addGroup(group: Group): Long

    @Update
    suspend fun updateGroup(group: Group)

    @Query("DELETE FROM 'group' WHERE groupId = :groupId")
    suspend fun deleteGroupById(groupId: Int)

//    @Query(
//        """
//        SELECT SUM(userexpense.balance)
//        FROM userexpense JOIN expense ON userexpense.expenseId = expense.expenseId
//        WHERE userId = :userId AND expense.groupId = :groupId
//        """
//    )
//    suspend fun getBalanceInGroup(userId: Int, groupId: Int): Double
}