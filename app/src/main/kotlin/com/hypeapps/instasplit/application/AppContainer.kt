package com.hypeapps.instasplit.application

import android.app.Activity
import android.content.Context
import androidx.room.Room
import com.hypeapps.instasplit.core.db.InstaSplitDatabase
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.network.RetrofitInstance
import com.hypeapps.instasplit.core.utils.OrientationManager
import com.hypeapps.instasplit.core.utils.UserManager

class AppContainer {
    lateinit var orientationManager: OrientationManager
    private val remoteDataSource = RetrofitInstance.instaSplitApi
    private lateinit var localDataSource: InstaSplitDatabase
    lateinit var repository: InstaSplitRepository
    lateinit var userManager: UserManager

    fun initDatabase(context: Context) {
        localDataSource =
            Room.databaseBuilder(
                context,
                InstaSplitDatabase::class.java, "instasplit_database"
            )
                .fallbackToDestructiveMigration() //TODO GET RID OF THIS LATER
                .build()
        repository = InstaSplitRepository(localDataSource, remoteDataSource)
    }

    fun initOrientationController(activity: Activity) {
        orientationManager = OrientationManager(activity)
    }

    fun initUserManager(context: Context) {
        userManager = UserManager(context)
    }

}