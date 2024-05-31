package com.jetbrains.kmpapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun requestPermission(
    permission: Permission,
    openSettingsWhenDeniedAlways: Boolean = false,
    onPermissionDeniedAlways: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    onPermissionGranted: () -> Unit = {},
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController = remember(factory) {
        factory.createPermissionsController()
    }

    BindEffect(controller)

    coroutineScope.launch {
        try {
            controller.providePermission(permission)
            onPermissionGranted()
        } catch (deniedAlways: DeniedAlwaysException) {
            println("THISISTAG DeniedAlwaysException ${deniedAlways.message}")
            if (openSettingsWhenDeniedAlways) onPermissionDeniedAlways()
        } catch (denied: DeniedException) {
            println("THISISTAG DeniedException ${denied.message}")
            onPermissionDenied()
        }
    }

}