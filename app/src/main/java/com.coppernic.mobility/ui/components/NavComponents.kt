package com.coppernic.mobility.ui.components

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.coppernic.mobility.util.constants.MainDestination

@Stable
@Composable
fun NavController.currentScreenAsState():State<String> {
    val selectedItem = remember { mutableStateOf<String>(MainDestination.HOME_ROUTE) }

    DisposableEffect(this) {
        Log.d("SHOW_ROUTE",selectedItem.value)
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.route == MainDestination.MARKINGS_ROUTE -> {
                    selectedItem.value = MainDestination.MARKINGS_ROUTE
                }
                destination.route == MainDestination.MUSTERING_ROUTE -> {
                    selectedItem.value = MainDestination.MUSTERING_ROUTE
                }
                destination.route == MainDestination.SERVER_ROUTE  -> {
                    selectedItem.value = MainDestination.SERVER_ROUTE
                }
                destination.route == MainDestination.CONFIGURATION_ROUTE  -> {
                    selectedItem.value = MainDestination.CONFIGURATION_ROUTE
                }
                destination.route == MainDestination.HOME_ROUTE  -> {
                    selectedItem.value = MainDestination.HOME_ROUTE
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    Log.d("SHOW_ROUTE",selectedItem.value)
    return selectedItem
}