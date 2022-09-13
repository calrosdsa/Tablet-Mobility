package com.coppernic.mobility.ui.screens.accesss

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AccessScreen(
    viewModel: AccessViewModel = hiltViewModel()
){
    Text(text = "This is the AccessScreen")
}