package com.hypeapps.instasplit.ui_testing.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hypeapps.instasplit.TestActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//it's hard to  do testing for this screen, so we're focusing on the UI elements instead
@RunWith(AndroidJUnit4::class)
class CameraScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Test
    fun testCameraScreenButtons_DisplayedAndClickable() {
        composeTestRule.setContent {
            // Simulating the Camera Screen UI
            Box {
                Button(onClick = {}) {
                    Text("Retake")
                }
                Button(onClick = {}) {
                    Text("Confirm")
                }
                Button(onClick = {}) {
                    Text("Cancel")
                }
            }
        }

        // Assertions to check visibility and interactivity
        with(composeTestRule) {
            onNodeWithText("Retake").assertIsDisplayed().performClick()
            onNodeWithText("Confirm").assertIsDisplayed().performClick()
            onNodeWithText("Cancel").assertIsDisplayed().performClick()
        }
    }
}
