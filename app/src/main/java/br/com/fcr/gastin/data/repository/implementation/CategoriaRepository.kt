package br.com.fcr.gastin.data.repository.implementation

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.MyDatabase
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.repository.ICategoriaRepository

class CategoriaRepository constructor(
    private val db:MyDatabase
): ICategoriaRepository{
    override fun getAll(): LiveData<List<Categoria>> {
        return db.getCategoriaDao().getAll()
    }

    override fun getById(ID: Int): LiveData<Categoria?> {
        return db.getCategoriaDao().getById(ID)
    }

    override fun insert(it: Categoria) {
        db.getCategoriaDao().insert(it)
    }

    override fun delete(ID: Int) {
        db.getCategoriaDao().delete(ID)
    }

}