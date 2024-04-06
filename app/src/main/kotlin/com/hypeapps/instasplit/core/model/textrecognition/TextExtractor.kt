package com.hypeapps.instasplit.core.model.textrecognition

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextExtractor  {
    private val imageProcessor = TextRecognitionProcessor(TextRecognizerOptions.Builder().build())

    fun extractText(bitmap: Bitmap): Task<Text> {
        return imageProcessor.processBitmap(bitmap)
    }

    fun close() {
        imageProcessor.stop()
    }
}