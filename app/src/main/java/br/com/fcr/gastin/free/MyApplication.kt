package br.com.fcr.gastin.free

import android.app.Application
import br.com.fcr.gastin.free.data.database.DB
import br.com.fcr.gastin.free.data.database.MyDatabase
import br.com.fcr.gastin.free.data.database.repository.ICategoriaRepository
import br.com.fcr.gastin.free.data.database.repository.IRegistroRepository
import br.com.fcr.gastin.free.data.database.repository.implementation.CategoriaRepository
import br.com.fcr.gastin.free.data.database.repository.implementation.RegistroRepository


class MyApplication : Application() {
    lateinit var myDatabase: MyDatabase
    lateinit var categoriaRepository: ICategoriaRepository
    lateinit var registroRepository: IRegistroRepository
    @Override
    override fun onCreate() {
        super.onCreate()
        myDatabase = DB.getService(this)
        categoriaRepository = CategoriaRepository(myDatabase)
        registroRepository = RegistroRepository(myDatabase)
    }

}