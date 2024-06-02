package com.jetbrains.kmpapp.screens.camera_permission

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.jetbrains.kmpapp.screens.camera_permission.UiPermissionState.ALWAYS_DENIED
import com.jetbrains.kmpapp.screens.camera_permission.UiPermissionState.DENIED_ONCE
import com.jetbrains.kmpapp.screens.camera_permission.UiPermissionState.GRANTED
import com.jetbrains.kmpapp.screens.camera_permission.UiPermissionState.INITIAL
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraPermissionViewModel(
    private val permissionsController: PermissionsController
) : ScreenModel {

    private val _state = MutableStateFlow(CameraPermissionState(INITIAL))
    val state = _state.asStateFlow()

    fun requestPermission() {
        screenModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _state.update { it.copy(uiPermissionState = GRANTED) }
            } catch (deniedAlways: DeniedAlwaysException) {
                _state.update {
                    it.copy(
                        uiPermissionState = ALWAYS_DENIED,
                        isAlwaysDeniedDialogVisible = true
                    )
                }
            } catch (denied: DeniedException) {
                _state.update { it.copy(uiPermissionState = DENIED_ONCE) }
            }
        }
    }

    fun openSettings() {
        permissionsController.openAppSettings()
    }

    fun onDismissDialog() {
        _state.update { it.copy(isAlwaysDeniedDialogVisible = false) }
    }

    suspend fun checkIfHavePermission(): Boolean {
        return permissionsController.isPermissionGranted(Permission.CAMERA)
    }
}

data class CameraPermissionState(
    val uiPermissionState: UiPermissionState,
    val isAlwaysDeniedDialogVisible: Boolean = false
)

/**
 * UI layer version of Moko permission state
 * */
enum class UiPermissionState {
    GRANTED,
    ALWAYS_DENIED,
    DENIED_ONCE,
    INITIAL
}