package com.developer.amukovozov.nerd.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ru.developer.amukovozov.nerd.ui.theme.*

private val DarkColorPalette = darkColors(
    primary = backgroundAccentColor,
    primaryVariant = primaryLightColor,
    onPrimary = white,
    secondary = primaryColor,
    secondaryVariant = primaryLightColor,
    onSecondary = white,
//    surface = backgroundColor,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
)

@Composable
fun NerdTheme(content: @Composable () -> Unit) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}