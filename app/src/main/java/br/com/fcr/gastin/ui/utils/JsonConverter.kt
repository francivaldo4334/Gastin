package br.com.fcr.gastin.ui.utils

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

inline fun <reified T: Any> toJson(obj: T) : String {
    val jsonObject = JSONObject()
    T::class.members.forEach {
        when (it) {
            is KProperty<*> -> {
                val value = it.getter.call(obj)
                when (value) {
                    is Int , is String ->
                        jsonObject.put(it.name, value)
                    is Date -> {
                        val format = SimpleDateFormat("yyyy-MM-dd")
                        val dateString = format.format(value)
                        jsonObject.put(it.name, dateString)
                    }
                    else ->
                        jsonObject.put(it.name, value.toString())
                }
            }
        }
    }
    return jsonObject.toString()
}