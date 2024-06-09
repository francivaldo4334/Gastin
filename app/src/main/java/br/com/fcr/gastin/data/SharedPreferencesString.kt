package br.com.fcr.gastin.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.activity.ComponentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesInt(
    private val context: Context,
    private val name: String,
    private val defaultValues: Int
) : ReadWriteProperty<Any?, Int> {
    private val sharedPreferences by lazy {
        context.getSharedPreferences("Preferences", MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return sharedPreferences.getInt(name,defaultValues)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        sharedPreferences.edit().putInt(name,value).apply()
    }
}
class SharedPreferencesString(
    private val context: Context,
    private val name: String,
    private val defaultValues: String
) : ReadWriteProperty<Any?, String> {
    private val sharedPreferences by lazy {
        context.getSharedPreferences("Preferences", MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return sharedPreferences.getString(name,defaultValues) ?: defaultValues
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        sharedPreferences.edit().putString(name,value).apply()
    }
}
class SharedPreferencesBoolean(
    private val context: Context,
    private val name: String,
    private val defaultValues: Boolean
) : ReadWriteProperty<Any?, Boolean> {
    private val sharedPreferences by lazy {
        context.getSharedPreferences("Preferences", MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return sharedPreferences.getBoolean(name,defaultValues)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        sharedPreferences.edit().putBoolean(name,value).apply()
    }
}
fun Context.sharedPreferences(name: String, defaultValues: String = "") = SharedPreferencesString(this,name, defaultValues)
fun Context.sharedPreferences(name: String, defaultValues: Boolean = false) = SharedPreferencesBoolean(this,name, defaultValues)
fun Context.sharedPreferences(name: String, defaultValues: Int = 0) = SharedPreferencesInt(this,name,defaultValues)