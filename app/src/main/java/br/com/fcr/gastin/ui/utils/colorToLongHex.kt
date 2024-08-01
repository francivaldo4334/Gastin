package br.com.fcr.gastin.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun colorToLongHex(color: Color): Long {
    return color.toArgb().toLong()
}
fun Color.toLongHex(): Long = colorToLongHex(this)