package com.hypeapps.instasplit.core.model.textrecognition

import VisionProcessorBase
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface


class TextRecognitionProcessor(
    textRecognizerOptions: TextRecognizerOptionsInterface
) : VisionProcessorBase<Text>() {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)

    override fun stop() {
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(results: Text) {
        Log.d(TAG, "On-device Text detection successful")
        logExtrasForTesting(results)
        getElementsAsList(results)
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Text detection failed.$e")
    }

    companion object {
        private const val TAG = "TextRecProcessor"
        private const val MANUAL_TESTING_LOG = "ManualTestLog"

        private fun getElementsAsList(results: Text): List<Text.Element> {
            val res = mutableListOf<Text.Element>()
            for (block in results.textBlocks) {
                for (line in block.lines) {
                    for (element in line.elements) {
                        res.add(element)
                    }
                }
            }
            return res
        }

        private fun logExtrasForTesting(results: Text?) {
            if (results != null) {
                Log.v(MANUAL_TESTING_LOG, "Detected text has : " + results.textBlocks.size + " blocks")
                for (i in results.textBlocks.indices) {
                    val lines = results.textBlocks[i].lines
                    Log.v(
                        MANUAL_TESTING_LOG,
                        String.format("Detected text block %d has %d lines", i, lines.size)
                    )
                    for (j in lines.indices) {
                        val elements = lines[j].elements
                        Log.v(
                            MANUAL_TESTING_LOG,
                            String.format("Detected text line %d has %d elements", j, elements.size)
                        )
                        for (k in elements.indices) {
                            val element = elements[k]
                            Log.v(MANUAL_TESTING_LOG,"--------------------------------------")
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format("Detected text element %d says: %s", k, element.text)
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Detected text element %d has a bounding box: %s",
                                    k,
                                    element.boundingBox!!.flattenToString()
                                )
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Expected corner point size is 4, get %d",
                                    element.cornerPoints!!.size
                                )
                            )
                            for (point in element.cornerPoints!!) {
                                Log.v(
                                    MANUAL_TESTING_LOG,
                                    String.format(
                                        "Corner point for element %d is located at: x - %d, y = %d",
                                        k,
                                        point.x,
                                        point.y
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
