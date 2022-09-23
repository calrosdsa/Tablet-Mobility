package com.coppernic.mobility.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.coppernic.mobility.ui.components.DrawerContentScreen
import com.coppernic.mobility.ui.components.currentScreenAsState
import com.coppernic.mobility.util.constants.MainDestination
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Home(
    initialRoute:String
){
    val navController = rememberAnimatedNavController()
    val state = rememberScaffoldState()

    Scaffold(
        scaffoldState = state,
        drawerContent = {
            DrawerContentScreen(navController = navController, scaffoldState =state)
        },
        drawerGesturesEnabled = false
    ) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = initialRoute){
                homeGraph(navController, state)
            }
        }
    }

}