package br.com.fcr.gastin.free.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = primary_dark,
    error = Color.Red,
    onError = white
)

private val LightColorPalette = lightColors(
    primary = primary_light,
)

@Composable
fun GastinTheme(darkTheme: Boolean = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES, content: @Composable () -> Unit) {
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