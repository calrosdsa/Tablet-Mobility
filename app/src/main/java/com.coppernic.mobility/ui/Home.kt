package com.coppernic.mobility.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
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
//                enterTransition = { defaultEnterTransition(initialState, targetState) },
//                exitTransition = { defaultExitTransition(initialState, targetState) },
                enterTransition = { defaultEnterTransition() },
                exitTransition = { defaultExitTransition() },
                popEnterTransition = { defaultPopEnterTransition() },
                popExitTransition = { defaultPopExitTransition() },
                startDestination = initialRoute){
                homeGraph(navController, state)
            }
        }
    }

}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
//    initial: NavBackStackEntry,
//    target: NavBackStackEntry
): EnterTransition {
//    val initialNavGraph = initial.destination.hostNavGraph
//    val targetNavGraph = target.destination.hostNavGraph
//    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
//    if (initialNavGraph.id != targetNavGraph.id) {
//        return fadeIn()
//    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
//    initial: NavBackStackEntry,
//    target: NavBackStackEntry
): ExitTransition {
//    val initialNavGraph = initial.destination.hostNavGraph
//    val targetNavGraph = target.destination.hostNavGraph
//    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
//    if (initialNavGraph.id != targetNavGraph.id) {
//        return fadeOut()
//    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}

