package com.hypeapps.instasplit.application

import android.app.Activity
import com.hypeapps.instasplit.ui.OrientationController

class AppContainer {
    lateinit var orientationController: OrientationController

    fun initOrientationController(activity: Activity) {
        orientationController = OrientationController(activity)
    }

}