package br.com.fcr.gastin.data.repository.implementation

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.MyDatabase
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.data.repository.IRegistroRepository
import kotlinx.coroutines.flow.Flow

class RegistroRepository constructor(
    private val db: MyDatabase
) : IRegistroRepository {
    override fun getAllDespesas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllDespesas()
    }

    override fun getAllReceitas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllReceitas()
    }

    override fun getById(ID: Int): Flow<Registro> {
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

    override fun getRegistrosByCategoriaId(id: Int): Flow<Int> {
        return db.getRegistroDao().getRegistrosByCategoriaId(id)
    }

    override fun update(resgister: Registro) {
        db.getRegistroDao().update(resgister)
    }

    override fun deleteAll(ids: List<Int>) {
        db.getRegistroDao().deleteAll(ids)
    }

    override fun getAllDespesasValor(): Flow<Int?>{
        return db.getRegistroDao().getAllDespesasValor()
    }
    override fun getAllReceitasValor(): Flow<Int?>{
        return db.getRegistroDao().getAllReceitasValor()
    }

    override fun getAllDespesasValorMesAno(mes: Int, ano: Int): Flow<Int?> {
        return return db.getRegistroDao().getAllDespesasValorMesAno(mes,ano)
    }

    override fun getAllReceitasValorMesAno(mes: Int, ano: Int): Flow<Int?> {
        return return db.getRegistroDao().getAllReceitasValorMesAno(mes,ano)
    }

}