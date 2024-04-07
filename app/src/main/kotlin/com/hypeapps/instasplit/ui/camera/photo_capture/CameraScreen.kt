package com.hypeapps.instasplit.ui.camera.photo_capture

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.core.utils.rotateBitmap
import kotlinx.coroutines.delay
import java.util.concurrent.Executor


@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(factory = CameraViewModel.Factory)
) {
    val cameraState: CameraState by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        viewModel.lockOrientation()
        onDispose {
            viewModel.closeTextExtractor()
            viewModel.unlockOrientation()
        }
    }

    CameraContent(
        onPhotoCaptured = viewModel::extractText, lastCapturedPhoto = cameraState.capturedImage
    )
}

@Composable
private fun CameraContent(
    onPhotoCaptured: (Bitmap) -> Unit, lastCapturedPhoto: Bitmap? = null
) {

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        FloatingActionButton(modifier = Modifier
            .wrapContentSize()
            .padding(bottom = 16.dp),
            onClick = { capturePhoto(context, cameraController, onPhotoCaptured) },
            containerColor = Color.White.copy(alpha = 0.0f),
            content = {
                Box(Modifier.border(width = 2.dp, color = Color.White, shape = CircleShape)) {
                    Icon(
                        modifier = Modifier
                            .size(64.dp)
                            .padding(4.dp), imageVector = Icons.Default.Camera, tint = Color.White, contentDescription = "Camera capture icon"
                    )
                }
            })
    }) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setBackgroundColor(Color.Black.toArgb())
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            })

            if (lastCapturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(alignment = BottomStart), lastCapturedPhoto = lastCapturedPhoto
                )
            }
        }
    }
}

private fun capturePhoto(
    context: Context, cameraController: LifecycleCameraController, onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        fun fixedRotation(image: ImageProxy): Int {
            return when (image.imageInfo.rotationDegrees) {
                0 -> 180
                90 -> 90
                180 -> 0
                270 -> 270
                else -> 0
            }
        }

        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image.toBitmap().rotateBitmap(fixedRotation(image))

            onPhotoCaptured(correctedBitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier, lastCapturedPhoto: Bitmap
) {

    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
    var isFullScreenPreview by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = capturedPhoto) {
        isFullScreenPreview = true
        delay(100)
        isFullScreenPreview = false
    }

    val bitmapDimensions = capturedPhoto.width to capturedPhoto.height
    // smallest of two dimensions should be 128 dp
    val bitmapAspectRatio = bitmapDimensions.first.toFloat() / bitmapDimensions.second
    val currentScreenWidth = LocalContext.current.resources.displayMetrics.widthPixels
    val previewWidth = animateDpAsState(
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow), targetValue = if (isFullScreenPreview) {
            currentScreenWidth.dp
        } else {
            128.dp
        }, label = ""
    )

    //TODO: make last captured photo larger and show extracted text boxes, then have button to confirm or retake

    Card(
        modifier = modifier
            .width(previewWidth.value)
            .aspectRatio(bitmapAspectRatio)
            .padding(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = MaterialTheme.shapes.small
    ) {
        Image(
            bitmap = capturedPhoto, contentDescription = "Last captured photo", contentScale = ContentScale.Crop
        )
    }
}
