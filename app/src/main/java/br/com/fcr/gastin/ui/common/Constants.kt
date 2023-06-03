package br.com.fcr.gastin.ui.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Constants {
    val OPEN_REGISTRO: String = "OPEN_REGISTRO"
    var IsDarkTheme by mutableStateOf(false)
    var IsTotalPeriod by mutableStateOf(false)
    val PREFERENCES = "PREFERENCES"
    val IS_DARKTHEM = "theme"
}