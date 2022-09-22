package com.coppernic.mobility.ui.screens.qr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun QrScreen(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: QrViewModel = hiltViewModel()
){
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = viewModel.qr_result.toString(),
        modifier = Modifier.align(Alignment.Center))
    }
}