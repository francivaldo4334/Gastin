package br.com.fcr.gastin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.fcr.gastin.data.dao.CategoriaDao
import br.com.fcr.gastin.data.dao.RegistroDao
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.data.resource.Converters

@Database(
    entities = arrayOf(
        Categoria::class,
        Registro::class
    ),
    version = 3
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    companion object{
        val NAME:String = "GASTIN_DATABASE"
    }

    abstract fun getRegistroDao():RegistroDao
    abstract fun getCategoriaDao():CategoriaDao
}