package br.com.fcr.gastin.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Constants {
    var IsDarkTheme by mutableStateOf(false)
    val PREFERENCES = "PREFERENCES"
    val DARK_THEME = "DARK_THEME"
    val LIGHT_THEME = "DARK_THEME"
    val CUSTOM_THEME = "THEME_DARK"
}