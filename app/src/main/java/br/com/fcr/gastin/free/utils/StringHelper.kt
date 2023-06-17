package br.com.fcr.gastin.free.ui.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
object Route{
    val HELP_SCREEN = "HELP_SCREEN"
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
@SuppressLint("SimpleDateFormat")
fun Date.toStringDate(it:String = "dd/MM/yyyy"):String{
    val format = SimpleDateFormat(it)
    return format.format(this)
}