package com.coppernic.mobility.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.coppernic.mobility.util.constants.MainDestination


sealed class HomeNavigationItem(
    val screen: String,
    val title:String
) {
    class ImageVectorIcon(
        screen: String,
        title: String,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(screen,title)
}

val navigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.HOME_ROUTE,
        title = "Inicio",
        iconImageVector = Icons.Outlined.Home,
        selectedImageVector = Icons.Default.Home,
    ),
    HomeNavigationItem.ImageVectorIcon(
//        screen = MainDestination.CIUDADES_SCREEN,
        screen = MainDestination.CIUDADES_SCREEN,
        title = "Mustering",
        iconImageVector = Icons.Outlined.Group,
        selectedImageVector = Icons.Default.Group,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.MARKINGS_ROUTE,
        title = "Marcaciones",
        iconImageVector = Icons.Outlined.CreditCard,
        selectedImageVector = Icons.Default.CreditCard,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.USERS_ROUTE,
        title = "Personal",
        iconImageVector = Icons.Outlined.Person,
        selectedImageVector = Icons.Default.Person,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.CONSULTA_SCREEN,
        title = "Consultas",
        iconImageVector = Icons.Outlined.QueryStats,
        selectedImageVector = Icons.Default.QueryStats
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = MainDestination.CONFIGURATION_ROUTE,
        title = "Configuraciones",
        iconImageVector = Icons.Outlined.Settings,
        selectedImageVector = Icons.Default.Settings,
        ),
)