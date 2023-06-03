package br.com.fcr.gastin.ui.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.R
import br.com.fcr.gastin.data.model.Categoria
import kotlinx.coroutines.flow.MutableStateFlow

fun List<Categoria>.toFlowTriper(): MutableStateFlow<List<Triple<String, Int, Color>>> {
    var resp = this.map {
        Triple(
            it.Name,
            it.total,
            Color(it.Color)
        )
    }
    return MutableStateFlow(resp)
}
fun Pair<Int,Int>.toFlowMonth(context:Context):MutableStateFlow<String>{
    var resp = when(this.first){
        1 -> context.getString(R.string.txt_january)
        2 -> context.getString(R.string.txt_february)
        3 -> context.getString(R.string.txt_march)
        4 -> context.getString(R.string.txt_april)
        5 -> context.getString(R.string.txt_may)
        6 -> context.getString(R.string.txt_june)
        7 -> context.getString(R.string.txt_july)
        8 -> context.getString(R.string.txt_august)
        9 -> context.getString(R.string.txt_september)
        10 -> context.getString(R.string.txt_october)
        11 -> context.getString(R.string.txt_november)
        12 -> context.getString(R.string.txt_december)
        else -> ""
    }
    return MutableStateFlow(resp)
}