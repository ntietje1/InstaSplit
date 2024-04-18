package com.hypeapps.instasplit.ui_testing.spash

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hypeapps.instasplit.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class SplashScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun splashScreenDisplaysAppTitle() {
        composeTestRule
            .onNodeWithText("InstaSplit", useUnmergedTree = true)
            .assertExists("The app title 'InstaSplit' should be displayed on the splash screen.")
    }

    @Test
    fun splashScreenDisplaysManageYourBillsMessage() {
        composeTestRule
            .onNodeWithText("Manage your Bills with InstaSplit")
            .assertExists("The message to manage bills should be displayed on the splash screen.")
    }

    @Test
    fun splashScreenProceedIconIsClickable() {
        composeTestRule
            .onNodeWithContentDescription("Proceed")
            .assertExists("The 'Proceed' icon should be displayed on the splash screen.")
            .performClick() // This will simulate a click action.
    }


    @Test
    fun splashScreenDisplaysDetailedDescription() {
        composeTestRule
            .onNodeWithText("Easily manage your debt, simplify shared expenses, and enjoy stress-free group financials!")
            .assertExists("The detailed description should be displayed on the splash screen.")
    }
}