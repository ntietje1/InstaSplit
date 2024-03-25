package com.hypeapps.instasplit.core.textrecognition

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hypeapps.instasplit.data.usecases.UseCase
import org.koin.core.annotation.Factory


@Factory
class TextExtractionUseCase(private val context: Context): UseCase {
    private val imageProcessor = TextRecognitionProcessor(context, TextRecognizerOptions.Builder().build())

    override suspend fun call(bitmap: Bitmap): Result<Unit> {
        imageProcessor.processBitmap(bitmap)
        return Result.success(Unit)
    }
}