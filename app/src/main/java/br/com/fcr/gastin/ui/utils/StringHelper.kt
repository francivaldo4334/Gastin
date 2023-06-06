package br.com.fcr.gastin.ui.utils

import java.text.NumberFormat
import java.util.*
object Route{
    val HOME = "HOME"
    val LISTA_DESPESAS = "LISTA_DESPESAS"
    val LISTA_RESEITAS = "LISTA_RESEITAS"
    val LISTA_CATEGORIAS = "LISTA_CATEGORIAS"
    val SPLASH_SCREEN = "SPLASH_SCREEN"
}
fun Int.toMonetaryString():String{
    val valorDouble = this.toDouble() / 100
    val locale = Locale("pt", "BR")
    val formato = NumberFormat.getCurrencyInstance(locale)
    val valorString = formato.format(valorDouble)
    return valorString
}