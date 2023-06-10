package br.com.fcr.gastin.free.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.fcr.gastin.free.data.database.dao.CategoriaDao
import br.com.fcr.gastin.free.data.database.dao.RegistroDao
import br.com.fcr.gastin.free.data.database.model.Categoria
import br.com.fcr.gastin.free.data.database.model.Registro
import br.com.fcr.gastin.free.data.database.resource.Converters

@Database(
    entities = arrayOf(
        Categoria::class,
        Registro::class
    ),
    version = 1
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    companion object{
        val NAME:String = "GASTIN_DATABASE"
    }

    abstract fun getRegistroDao(): RegistroDao
    abstract fun getCategoriaDao(): CategoriaDao
}