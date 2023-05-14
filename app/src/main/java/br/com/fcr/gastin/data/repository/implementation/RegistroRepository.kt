package br.com.fcr.gastin.data.repository.implementation

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.MyDatabase
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.data.repository.IRegistroRepository

class RegistroRepository constructor(
    private val db: MyDatabase
) : IRegistroRepository {
    override fun getAllDespesas(): LiveData<List<Registro>> {
        return db.getRegistroDao().getAllDespesas()
    }

    override fun getAllReceitas(): LiveData<List<Registro>> {
        return db.getRegistroDao().getAllReceitas()
    }

    override fun getById(ID: Int): LiveData<Registro> {
        return db.getRegistroDao().getById(ID)
    }

    override fun delete(ID: Int) {
        db.getRegistroDao().delete(ID)
    }

    override fun insert(it: Registro) {
        db.getRegistroDao().insert(it)
    }

    override fun getall(): LiveData<List<Registro>> {
        return db.getRegistroDao().getAll()
    }

}