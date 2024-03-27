package com.hypeapps.instasplit.core.model.usecases

import android.graphics.Bitmap

interface UseCase {
    suspend fun call(bitmap: Bitmap) : Result<Unit>
}