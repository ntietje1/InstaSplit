import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class VisionProcessorBase<T>(context: Context) {

    protected abstract fun detectInImage(image: InputImage): Task<T>

    protected abstract fun onSuccess(results: T)

    protected abstract fun onFailure(e: Exception)

    protected abstract fun stop()

    fun processBitmap(bitmap: Bitmap?) {
        val image = InputImage.fromBitmap(bitmap!!, 0)
        detectInImage(image)
            .addOnSuccessListener { results -> onSuccess(results) }
            .addOnFailureListener { e -> onFailure(e) }
    }


}