package com.hypeapps.instasplit.core.utils

import android.app.Activity
import android.content.pm.ActivityInfo

class OrientationManager(private val activity: Activity) {

    fun lockPortrait() {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    fun lockLandscape() {
        lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    private fun lockOrientation(orientation: Int) {
        activity.requestedOrientation = orientation
    }

    fun unlockOrientation() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}