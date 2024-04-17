package com.hypeapps.instasplit.ui.camera

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text
import com.hypeapps.instasplit.core.model.textrecognition.TextElementParser
import com.hypeapps.instasplit.core.model.textrecognition.TextExtractor
import com.hypeapps.instasplit.core.utils.OrientationManager
import com.hypeapps.instasplit.ui.camera.photo_capture.CameraViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CameraViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CameraViewModel
    private lateinit var textExtractor: TextExtractor
    private lateinit var textElementParser: TextElementParser
    private lateinit var orientationManager: OrientationManager
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        textExtractor = mockk(relaxed = true)
        textElementParser = mockk(relaxed = true)
        orientationManager = mockk(relaxed = true)

        viewModel = CameraViewModel(textExtractor, textElementParser, orientationManager)
    }

    @After
    fun tearDown() {
        viewModel.closeTextExtractor()  // Ensure resources are released
    }


    @Test
    fun `verify lockOrientation locks the orientation`() = runTest {
        viewModel.lockOrientation()
        verify(exactly = 1) { orientationManager.lockPortrait() }
    }

    @Test
    fun `verify unlockOrientation unlocks the orientation`() = runTest {
        viewModel.unlockOrientation()
        verify(exactly = 1) { orientationManager.unlockOrientation() }
    }


    @Test
    fun `verify resetImageCaptured clears captured image and extracted elements`() = runTest(testDispatcher) {
        viewModel.resetImageCaptured()
        val state = viewModel.state.value
        assertEquals(null, state.capturedImage)
        assertEquals(null, state.extractedElements)
    }
}
