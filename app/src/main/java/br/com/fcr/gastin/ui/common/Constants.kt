package br.com.fcr.gastin.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Constants {
    var IsDarkTheme by mutableStateOf(false)
    val PREFERENCES = "PREFERENCES"
    val IS_DARKTHEM = "theme"
}