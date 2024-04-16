package com.hypeapps.instasplit.ui.camera

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hypeapps.instasplit.ui.camera.no_permission.NoPermissionScreen
import com.hypeapps.instasplit.ui.camera.photo_capture.CameraScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraMainScreen(
    onResult: (Double) -> Unit,
    onBack: () -> Unit
) {
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        onResult = onResult,
        onBack = onBack
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onResult: (Double) -> Unit,
    onBack: () -> Unit
) {

    if (hasPermission) {
        CameraScreen(
            onResult = onResult,
            onBack = onBack
        )
    } else {
        NoPermissionScreen(onRequestPermission) //TODO add back button
    }
}
