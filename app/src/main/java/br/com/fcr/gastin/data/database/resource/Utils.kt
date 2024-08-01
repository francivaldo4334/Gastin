package br.com.fcr.gastin.data.database.resource

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar

fun getStartOfMonthTimestamp(year: Int, month: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getEndOfMonthTimestamp(year: Int, month: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, month + 1, 1, 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.timeInMillis
}

fun getStartOfWeekTimestamp(year: Int, weekOfYear: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Definindo o início da semana como segunda-feira
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getEndOfWeekTimestamp(year: Int, weekOfYear: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear + 1)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Definindo o fim da semana como domingo
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}