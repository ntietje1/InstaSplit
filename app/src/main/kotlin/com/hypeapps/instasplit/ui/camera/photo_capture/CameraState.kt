package com.hypeapps.instasplit.ui.camera.photo_capture

import android.graphics.Bitmap
import com.google.mlkit.vision.text.Text

data class CameraState(
    val capturedImage: Bitmap? = null,
    val extractedElements: Pair<Text.Element, Text.Element>? = null,
)
