package com.hypeapps.instasplit.core.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user WHERE userId = :userId")
    suspend fun deleteUserById(userId: Int)

    @Transaction
    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserWrapper(userId: Int): UserWrapper?

    @Transaction
    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserWrapperLiveData(userId: Int): LiveData<UserWrapper>


}