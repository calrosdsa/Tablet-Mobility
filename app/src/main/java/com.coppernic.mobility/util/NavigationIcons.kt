package com.coppernic.mobility.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.constants.Params


sealed class HomeNavigationItem(
    val screen: String,
    val title:String
) {
    class ImageVectorIcon(
        screen: String,
        title: String,
        val iconImageVector: ImageVector,
    ) : HomeNavigationItem(screen,title)
}

val navigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.HOME_ROUTE,
        title = "Inicio",
        iconImageVector = Icons.Outlined.Home,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.MARKINGS_ROUTE,
        title = "Marcaciones",
        iconImageVector = Icons.Outlined.CardMembership,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.USERS_ROUTE,
        title = "Personal",
        iconImageVector = Icons.Outlined.Person,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.CONSULTA_SCREEN,
        title = "Consultas",
        iconImageVector = Icons.Outlined.QueryStats,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.CAMERA_SCREEN,
        title = "Scanner Qr",
        iconImageVector = Icons.Outlined.QrCodeScanner
    ),
    HomeNavigationItem.ImageVectorIcon(
//        screen = MainDestination.CIUDADES_SCREEN,
        screen = MainDestination.CIUDADES_SCREEN,
        title = "Mustering",
        iconImageVector = Icons.Outlined.Group,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.ACCESS_ROUTE,
        title = "Configuraciones",
        iconImageVector = Icons.Outlined.Settings,
        ),
)