package br.com.fcr.gastin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.ui.common.Constants

private val DarkColorPalette = darkColors(
    primary = primary_dark,
    error = Color.Red,
    onError = white
)

private val LightColorPalette = lightColors(
    primary = primary_light,
)

@Composable
fun GastinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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