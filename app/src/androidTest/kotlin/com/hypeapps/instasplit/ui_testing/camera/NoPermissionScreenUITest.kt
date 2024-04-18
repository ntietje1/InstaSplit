package com.hypeapps.instasplit.ui_testing.camera

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hypeapps.instasplit.MainActivity
import com.hypeapps.instasplit.TestActivity
import com.hypeapps.instasplit.ui.camera.no_permission.NoPermissionScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NoPermissionScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun testPermissionScreenDisplaysCorrectly() {
        composeTestRule.setContent {
            NoPermissionScreen(onRequestPermission = {})
        }

        // Assertions can be made here to check if UI components display correctly
        composeTestRule.onNodeWithText("Please grant the permission to use the camera to use the receipt scanning functionality.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Grant permission")
            .assertIsDisplayed()
    }

    @Test
    fun testPermissionButtonTriggersAction() {
        var permissionRequested = false

        composeTestRule.setContent {
            NoPermissionScreen(onRequestPermission = { permissionRequested = true })
        }

        composeTestRule.onNodeWithText("Grant permission").performClick()

        assert(permissionRequested) {
            "Permission request action was not triggered."
        }
    }
}
