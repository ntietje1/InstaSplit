package com.hypeapps.instasplit.ui.camera.photo_capture

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.mlkit.vision.text.Text
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.model.textrecognition.TextElementParser
import com.hypeapps.instasplit.core.model.textrecognition.TextExtractor
import com.hypeapps.instasplit.ui.OrientationController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val textExtractor: TextExtractor, private val textElementParser: TextElementParser, private val orientationController: OrientationController
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun closeTextExtractor() {
        textExtractor.close()
    }

    fun lockOrientation() {
        orientationController.lockPortrait()
    }

    fun unlockOrientation() {
        orientationController.unlockOrientation()
    }

    fun onImageCaptured(bitmap: Bitmap) {
        extractText(bitmap)
    }

    private fun extractText(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            updateIsProcessing(true)
            textExtractor.extractText(bitmap).apply {
                addOnCompleteListener {
                    updateIsProcessing(false)
                }
                addOnSuccessListener { text ->
                    val nameAndPrice = textElementParser.getTotal(text)
                    updateExtractedElements(nameAndPrice)
                }
            }
        }
        updateCapturedPhotoState(bitmap)
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    private fun updateExtractedElements(elements: Pair<Text.Element, Text.Element>?) {
        _state.value = _state.value.copy(extractedElements = elements)
    }

    private fun updateIsProcessing(isProcessing: Boolean) {
        _state.value = _state.value.copy(isProcessing = isProcessing)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return CameraViewModel(
                    TextExtractor(), TextElementParser(), (application as App).appContainer.orientationController
                ) as T
            }
        }
    }
}
