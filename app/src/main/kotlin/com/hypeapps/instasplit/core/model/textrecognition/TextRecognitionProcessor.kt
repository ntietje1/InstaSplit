package com.hypeapps.instasplit.core.model.textrecognition

import VisionProcessorBase
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface


class TextRecognitionProcessor(
    textRecognizerOptions: TextRecognizerOptionsInterface
)  {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)

    fun stop() {
        textRecognizer.close()
    }

    private fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    private fun onSuccess(results: Text) {
        Log.d(TAG, "On-device Text detection successful")
        Log.v(MANUAL_TESTING_LOG, "GOT TOTAL: " + TextElementParser().getTotal(results)?.second?.text)
    }

    private fun onFailure(e: Exception) {
        Log.w(TAG, "Text detection failed.$e")
    }

    fun processBitmap(bitmap: Bitmap?): Task<Text> {
        val image = InputImage.fromBitmap(bitmap!!, 0)
        return detectInImage(image)
            .addOnSuccessListener { results -> onSuccess(results) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    companion object {
        private const val TAG = "TextRecProcessor"
        private const val MANUAL_TESTING_LOG = "ManualTestLog"
    }
}

