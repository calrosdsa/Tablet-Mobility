package com.coppernic.mobility.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.coppernic.mobility.ui.screens.SplashScreen
import com.coppernic.mobility.ui.screens.home.HomeScreen
import com.coppernic.mobility.ui.screens.accesss.AccessScreen
import com.coppernic.mobility.ui.screens.ciudades.CiudadesScreen
import com.coppernic.mobility.ui.screens.consulta.ConsultaScreen
import com.coppernic.mobility.ui.screens.detail.DetailScreen
import com.coppernic.mobility.ui.screens.manual_marker.ManualMarkerScreen
import com.coppernic.mobility.ui.screens.markings.MarkedsScreen
import com.coppernic.mobility.ui.screens.mustering.MusteringScreen
import com.coppernic.mobility.ui.screens.servidor.ServidorScreen
import com.coppernic.mobility.ui.screens.setting.SettingScreen
import com.coppernic.mobility.ui.screens.usuarios.UsersScreem
import com.coppernic.mobility.ui.screens.waiting.WaitingScreen
import com.coppernic.mobility.ui.screens.zones.ZoneScreen
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.constants.Params
import com.coppernic.mobility.ui.screens.estadoPerson.EstadoPerson
import com.google.accompanist.navigation.animation.composable

@ExperimentalAnimationApi
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    scaffoldState: ScaffoldState
){
    composable(MainDestination.HOME_ROUTE){
        HomeScreen(navController,scaffoldState)
    }
    composable(MainDestination.SPLASH_SCREEN){
        SplashScreen(navController)
    }
    composableDetail(MainDestination.ACCESS_ROUTE){
        AccessScreen()
    }
    composableDetail(MainDestination.ESTADO_PERSON + "?estado={${Params.ESTADO_PERSON_P}}&" +
            "ciudadId={${Params.CIUDAD_PARAM}}", arguments = listOf(
        navArgument(Params.ESTADO_PERSON_P){
            type = NavType.StringType
            nullable = true
        },
        navArgument(Params.CIUDAD_PARAM){
            type = NavType.StringType
            nullable = true
        })
    ){
        EstadoPerson(navController , scaffoldState)
    }
    composableDetail(MainDestination.WAITING_SCREEN + "?type_access={${Params.TYPE_ACCESS_PARAM}}&" +
            "facility_code={${Params.FACILITY_CODE_P}}&card_number={${Params.CARD_NUMBER_P}}",
        arguments = listOf(
            navArgument(Params.TYPE_ACCESS_PARAM){
                type = NavType.StringType
                nullable = true
            },
            navArgument(Params.FACILITY_CODE_P){
                type = NavType.StringType
                nullable = true
            },
            navArgument(Params.CARD_NUMBER_P){
                type = NavType.StringType
                nullable = true
            }
        )){
        WaitingScreen(navController = navController,scaffoldState)
    }
    composableDetail(MainDestination.MUSTERONG_ZONE+ "?zoneId={${Params.ZONE_PARAM}}&" +
            "ciudadId={${Params.CIUDAD_PARAM}}", arguments = listOf(
        navArgument(Params.ZONE_PARAM){
            type = NavType.StringType
            nullable = true
        },
        navArgument(Params.CIUDAD_PARAM){
            type = NavType.StringType
            nullable = true
        })
    ){
        ZoneScreen(navController , scaffoldState)
    }
    composableDetail(MainDestination.CONFIGURATION_ROUTE){
        SettingScreen(navController,scaffoldState)
    }
    composableDetail(MainDestination.MANUAL_ROUTE + "/{${Params.TYPE_ACCESS_PARAM}}"){
        ManualMarkerScreen(navController)
    }
    composableDetail(MainDestination.SERVER_ROUTE){
        ServidorScreen()
    }
    composableDetail(MainDestination.MUSTERING_ROUTE + "/{${Params.CIUDAD_PARAM}}"){
        MusteringScreen(navController,scaffoldState)
    }
    composableDetail(MainDestination.MARKINGS_ROUTE){
        MarkedsScreen(navController,scaffoldState)
//        PaginatedMarcacionesScreen(navController , scaffoldState )
    }
    composableDetail(MainDestination.CIUDADES_SCREEN){
        CiudadesScreen(navController,scaffoldState)
    }
    composableDetail(MainDestination.USER_DETAIL + "/{${Params.GUID_USER}}"){
        DetailScreen(navController)
    }
    composableDetail(MainDestination.USERS_ROUTE){
        UsersScreem(navController)
    }
    composableDetail(MainDestination.CONSULTA_SCREEN){
       ConsultaScreen(navController, scaffoldState)
    }

}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableDetail(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable () -> Unit,
    ) {

    composable(
        route = route,
        arguments = arguments,
        enterTransition = { slideIntoContainer(
            AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(500)
        )
        },
        popEnterTransition = { slideIntoContainer(
            AnimatedContentScope.SlideDirection.Right,
            animationSpec = tween(400)
        )
        },
        popExitTransition = { slideOutOfContainer(
            AnimatedContentScope.SlideDirection.Right,
            animationSpec = tween(400)
        )
        },
        exitTransition = { slideOutOfContainer(
            AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(500)
        )
        }
//                enterTransition = { slideInHorizontally(initialOffsetX = { 3000 }, animationSpec = tween(500))},
//        exitTransition = { slideOutHorizontally(targetOffsetX = { 3000 }, animationSpec = tween(500))},
        //       popEnterTransition = { slideInHorizontally(initialOffsetX = { 3000 }, animationSpec = tween(500))},
        //       popExitTransition= { slideOutHorizontally(targetOffsetX = { 3000 }, animationSpec = tween(500))},
    ) {
        content()
    }
}