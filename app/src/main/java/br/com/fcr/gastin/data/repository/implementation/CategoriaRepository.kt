package br.com.fcr.gastin.data.repository.implementation

import br.com.fcr.gastin.data.MyDatabase
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.repository.ICategoriaRepository
import kotlinx.coroutines.flow.Flow

class CategoriaRepository constructor(
    private val db:MyDatabase
): ICategoriaRepository{
    override fun getAll(): Flow<List<Categoria>> {
        return db.getCategoriaDao().getAll()
    }

    override fun getById(ID: Int): Flow<Categoria?> {
        return db.getCategoriaDao().getById(ID)
    }

    override fun insert(it: Categoria) {
        db.getCategoriaDao().insert(it)
    }

    override fun update(it: Categoria) {
        db.getCategoriaDao().update(it)
    }

    override fun delete(ID: Int) {
        db.getCategoriaDao().delete(ID)
    }

    override fun deleteAll(it: List<Int>) {
        db.getCategoriaDao().deleteAll(it)
    }
    override fun getAllWithTotal():Flow<List<Categoria>>{
        return db.getCategoriaDao().getAllWithTotal()
    }

}