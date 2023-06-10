package br.com.fcr.gastin.free.data.database.repository.implementation

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.free.data.database.MyDatabase
import br.com.fcr.gastin.free.data.database.model.Registro
import br.com.fcr.gastin.free.data.database.repository.IRegistroRepository
import br.com.fcr.gastin.free.data.database.viewmodel.DashboardWeek
import kotlinx.coroutines.flow.Flow

class RegistroRepository constructor(
    private val db: MyDatabase
) : IRegistroRepository {
    override fun getAllDespesas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllDespesas()
    }

    override fun getAllDespesasMesAno(mes: Int, ano: Int): Flow<List<Registro>> {
        return db.getRegistroDao().getAllDespesasMesAno(mes,ano)
    }

    override fun getAllReceitas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllReceitas()
    }

    override fun getAllReceitasMesAno(mes: Int, ano: Int): Flow<List<Registro>> {
        return db.getRegistroDao().getAllReceitasMesAno(mes,ano)
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
        return db.getRegistroDao().getAllDespesasValorMesAno(mes,ano)
    }

    override fun getAllReceitasValorMesAno(mes: Int, ano: Int): Flow<Int?> {
        return db.getRegistroDao().getAllReceitasValorMesAno(mes,ano)
    }
    override fun getDasboardWeek(week:Int,year:Int):Flow<List<DashboardWeek>>{
        return db.getRegistroDao().getDasboardWeek(week,year)
    }

    override fun getDasboardMonth(month: Int, year: Int): Flow<List<DashboardWeek>> {
        return db.getRegistroDao().getDasboardMonth(month,year)
    }

}