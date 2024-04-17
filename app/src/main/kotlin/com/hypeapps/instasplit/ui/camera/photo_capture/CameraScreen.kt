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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.text.Text
import com.hypeapps.instasplit.core.utils.rotateBitmap
import java.util.concurrent.Executor


@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(factory = CameraViewModel.Factory), onResult: (Double) -> Unit, onBack: () -> Unit
) {
    val cameraState: CameraState by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        viewModel.lockOrientation()
        onDispose {
            viewModel.closeTextExtractor()
            viewModel.unlockOrientation()
        }
    }



    if (cameraState.capturedImage != null) {
        ConfirmPhotoPreview(
            onConfirm = { onResult(cameraState.extractedElements!!.second.text.toDouble()) }, // guaranteed to be non-null & a valid double
            onRetake = viewModel::resetImageCaptured,
            onCancel = onBack,
            lastCapturedPhoto = cameraState.capturedImage!!,
            processing = cameraState.isProcessing,
            extractedName = cameraState.extractedElements?.first,
            extractedPrice = cameraState.extractedElements?.second
        )
    } else {
        CameraContent(onPhotoCaptured = viewModel::onImageCaptured)
    }
}

@Composable
private fun ConfirmPhotoPreview(
    onConfirm: () -> Unit, onRetake: () -> Unit, onCancel: () -> Unit, lastCapturedPhoto: Bitmap, processing: Boolean, extractedName: Text.Element?, extractedPrice: Text.Element?
) {
    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center), bitmap = capturedPhoto, contentDescription = "Last captured photo", contentScale = ContentScale.Fit
        )
        // dim and loading indicator while processing
        if (processing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (processing) {
                Text(
                    text = "Processing...", color = Color.White
                )
            } else if (extractedName != null && extractedPrice != null) {
                Text(
                    text = "Extracted Text:", color = Color.White
                )
                Text(
                    text = "${extractedName.text} : $${extractedPrice.text}", color = Color.White
                )
            } else {
                Text(
                    text = "Failed to extract :(", color = Color.White
                )
            }
        }
        if (!processing) {
            ActionButtonsRow(
                modifier = Modifier.align(Alignment.BottomCenter),
                onRetake = onRetake,
                onConfirm = onConfirm,
                onCancel = onCancel,
                extractedSuccessfully = extractedName != null && extractedPrice != null
            )
        }
    }
}

@Composable
private fun ActionButtonsRow(
    modifier: Modifier = Modifier,
    onRetake: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    extractedSuccessfully: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onRetake) {
            Row(Modifier.padding(4.dp)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Confirm")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Retake", textAlign = TextAlign.Center)
            }
        }
        if (extractedSuccessfully) {
            Button(onClick = onConfirm) {
                Row(Modifier.padding(4.dp)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Confirm", textAlign = TextAlign.Center)
                }
            }
        } else {
            Button(onClick = onCancel) {
                Row(Modifier.padding(4.dp)) {
                    Icon(imageVector = Icons.Default.Cancel, contentDescription = "Confirm")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Cancel", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun CameraContent(
    onPhotoCaptured: (Bitmap) -> Unit
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
//
//@Composable
//private fun LastPhotoPreview(
//    modifier: Modifier = Modifier, lastCapturedPhoto: Bitmap
//) {
//
//    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
//    var isFullScreenPreview by remember { mutableStateOf(true) }
//
//    val bitmapDimensions = capturedPhoto.width to capturedPhoto.height
//    // smallest of two dimensions should be 128 dp
//    val bitmapAspectRatio = bitmapDimensions.first.toFloat() / bitmapDimensions.second
//    val currentScreenWidth = LocalContext.current.resources.displayMetrics.widthPixels
//    val previewWidth = animateDpAsState(
//        animationSpec = if (isFullScreenPreview) {
//            snap()
//        } else {
//            spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessVeryLow)
//        }, targetValue = if (isFullScreenPreview) {
//            currentScreenWidth.dp
//        } else {
//            128.dp
//        }, label = ""
//    )
//
//    LaunchedEffect(key1 = capturedPhoto) {
//        isFullScreenPreview = true
//        delay(100)
//        isFullScreenPreview = false
//    }
//
//
//
//    Card(
//        modifier = modifier
//            .width(previewWidth.value)
//            .aspectRatio(bitmapAspectRatio)
//            .padding(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), shape = MaterialTheme.shapes.small
//    ) {
//        Image(
//            bitmap = capturedPhoto, contentDescription = "Last captured photo", contentScale = ContentScale.Crop
//        )
//    }
//}
