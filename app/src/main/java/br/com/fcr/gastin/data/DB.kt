package br.com.fcr.gastin.data

import android.content.Context
import androidx.room.Room

object DB {
    fun getService(context: Context): MyDatabase{
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            MyDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }
}