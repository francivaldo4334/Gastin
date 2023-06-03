package br.com.fcr.gastin.ui.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.R
import br.com.fcr.gastin.data.model.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date

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
fun Int.toWeekString(context:Context):String{
    var resp = when(this){
        1 -> context.getString(R.string.txt_Domingo)
        2 -> context.getString(R.string.txt_segunda_feira)
        3 -> context.getString(R.string.txt_terça_feira)
        4 -> context.getString(R.string.txt_quarta_feira)
        5 -> context.getString(R.string.txt_quinta_feira)
        6 -> context.getString(R.string.txt_sexta_feira)
        7 -> context.getString(R.string.txt_sabado)
        else -> ""
    }
    return resp
}

fun getDatesOfWeek(year: Int, week: Int): List<Date> {
    val calendar = Calendar.getInstance()
    calendar.clear()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.WEEK_OF_YEAR, week)

    val datesOfWeek = mutableListOf<Date>()

    // Definir o primeiro dia da semana como domingo
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    // Adicionar os 7 dias da semana à lista
    repeat(7) {
        datesOfWeek.add(calendar.time)
        calendar.add(Calendar.DAY_OF_WEEK, 1)
    }

    return datesOfWeek
}