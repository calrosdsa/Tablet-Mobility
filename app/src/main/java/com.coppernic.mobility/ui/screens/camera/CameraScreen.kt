package com.coppernic.mobility.ui.screens.camera

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavController,
    scaffoldState: ScaffoldState
){
    val cameraPermissions = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(key1 = true, block = {
        cameraPermissions.launchPermissionRequest()
    })
    Column {
        CameraPreview(navController)
    }
}