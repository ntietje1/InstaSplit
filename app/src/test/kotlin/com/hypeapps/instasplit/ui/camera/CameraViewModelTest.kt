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
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
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
    fun `orientation changes are respected`() = runTest {
        viewModel.lockOrientation()
        verify { orientationManager.lockPortrait() }

        viewModel.unlockOrientation()
        verify { orientationManager.unlockOrientation() }
    }



    @Test
    fun `verify resetImageCaptured clears captured image and extracted elements`() = runTest(testDispatcher) {
        viewModel.resetImageCaptured()
        val state = viewModel.state.value
        assertEquals(null, state.capturedImage)
        assertEquals(null, state.extractedElements)
    }

    @Test
    fun `onImageCaptured handles errors gracefully`() = runTest {
        val bitmapMock = mockk<Bitmap>(relaxed = true)
        val exception = RuntimeException("Test Exception")

        coEvery { textExtractor.extractText(any()) } throws exception

        viewModel.onImageCaptured(bitmapMock)

        // Check if state reflects processing ended and no results
        assert(viewModel.state.value.capturedImage === bitmapMock)
        assert(viewModel.state.value.extractedElements == null)
        assert(!viewModel.state.value.isProcessing)
    }

    @Test
    fun `text extraction is triggered with correct bitmap`() = runTest {
        val bitmapMock = mockk<Bitmap>(relaxed = true)
        coEvery { textExtractor.extractText(bitmapMock) } returns mockk(relaxed = true)

        viewModel.onImageCaptured(bitmapMock)

        coVerify(exactly = 1) { textExtractor.extractText(bitmapMock) }
    }

    @Test
    fun `handle text extraction failure gracefully`() = runTest {
        val bitmapMock = mockk<Bitmap>(relaxed = true)
        val taskMock: Task<Text> = mockk(relaxed = true)
        coEvery { textExtractor.extractText(bitmapMock) } returns taskMock
        every { taskMock.isSuccessful } returns false

        viewModel.onImageCaptured(bitmapMock)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(bitmapMock, state.capturedImage)
        assertNull(state.extractedElements)
        assertFalse(state.isProcessing)
    }
}
