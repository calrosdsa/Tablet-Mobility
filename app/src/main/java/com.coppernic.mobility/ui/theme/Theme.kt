    package com.coppernic.mobility.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver


private val Slate200 = Color(0xFF81A9B3)
private val Slate600 = Color(0xFF4A6572)

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple200,
    secondary = Slate200,
    background = Color.Black,
    secondaryVariant = Color.White,
    onPrimary = Color.LightGray,
    onSecondary = Color.DarkGray
).withBrandedSurface()

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Slate600,
    background = Color.White,
    secondaryVariant = Color.Black,
    onPrimary = Color.DarkGray,
    onSecondary = Color.LightGray

    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

fun Colors.withBrandedSurface() = copy(
    surface = background.copy(alpha = 0.08f).compositeOver(this.surface),
)

@Composable
fun TecluMobilityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}