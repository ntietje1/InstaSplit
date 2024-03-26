package com.hypeapps.instasplit.data.usecases

import android.graphics.Bitmap
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hypeapps.instasplit.core.textrecognition.TextRecognitionProcessor


class TextExtractionUseCase: UseCase {
    private val imageProcessor = TextRecognitionProcessor(TextRecognizerOptions.Builder().build())

    override suspend fun call(bitmap: Bitmap): Result<Unit> {
        imageProcessor.processBitmap(bitmap)
        return Result.success(Unit)
    }
}