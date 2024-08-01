package br.com.fcr.gastin.data.database.repository.implementation

import br.com.fcr.gastin.data.database.MyDatabase
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.database.repository.ICategoriaRepository
import br.com.fcr.gastin.data.database.resource.getEndOfMonthTimestamp
import br.com.fcr.gastin.data.database.resource.getStartOfMonthTimestamp
import kotlinx.coroutines.flow.Flow

class CategoriaRepository constructor(
    private val db: MyDatabase
): ICategoriaRepository {
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
    override fun getAllWithMesAno(mes:Int,ano:Int):Flow<List<Categoria>>{
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(ano, mes) to getEndOfMonthTimestamp(ano, mes)
        return db.getCategoriaDao().getAllWithMesAno(startTimestamp, endTimestamp)
    }

}