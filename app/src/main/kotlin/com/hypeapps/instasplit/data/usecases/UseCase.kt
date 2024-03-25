package com.hypeapps.instasplit.data.usecases

import android.graphics.Bitmap

interface UseCase {
    suspend fun call(bitmap: Bitmap) : Result<Unit>
}