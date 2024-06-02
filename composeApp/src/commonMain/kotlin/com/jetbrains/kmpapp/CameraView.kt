package com.jetbrains.kmpapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.icerock.moko.permissions.Permission

@Composable
fun CameraScreen() {
    var hasCameraPermission by remember { mutableStateOf(false) }
    requestPermission(
        permission = Permission.CAMERA,
        openSettingsWhenDeniedAlways = true
    ) {
        hasCameraPermission = true
    }

    if (hasCameraPermission) CameraView()
}

@Composable
expect fun CameraView()