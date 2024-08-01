package br.com.fcr.gastin.data.database.repository.implementation

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.database.MyDatabase
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.data.database.repository.IRegistroRepository
import br.com.fcr.gastin.data.database.resource.getStartOfMonthTimestamp
import br.com.fcr.gastin.data.database.resource.getEndOfMonthTimestamp
import br.com.fcr.gastin.data.database.viewmodel.DashboardWeek
import kotlinx.coroutines.flow.Flow

class RegistroRepository constructor(
    private val db: MyDatabase
) : IRegistroRepository {
    override fun getAllDespesas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllDespesas()
    }

    override fun getAllDespesasMesAno(mes: Int, ano: Int): Flow<List<Registro>> {
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(ano, mes) to getEndOfMonthTimestamp(ano, mes)
        return db.getRegistroDao().getAllDespesasMesAno(startTimestamp, endTimestamp)
    }

    override fun getAllReceitas(): Flow<List<Registro>> {
        return db.getRegistroDao().getAllReceitas()
    }

    override fun getAllReceitasMesAno(mes: Int, ano: Int): Flow<List<Registro>> {
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(ano, mes) to getEndOfMonthTimestamp(ano, mes)
        return db.getRegistroDao().getAllReceitasMesAno(startTimestamp, endTimestamp)
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
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(ano, mes) to getEndOfMonthTimestamp(ano, mes)
        return db.getRegistroDao().getAllDespesasValorMesAno(startTimestamp, endTimestamp)
    }

    override fun getAllReceitasValorMesAno(mes: Int, ano: Int): Flow<Int?> {
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(ano, mes) to getEndOfMonthTimestamp(ano, mes)
        return db.getRegistroDao().getAllReceitasValorMesAno(startTimestamp, endTimestamp)
    }
    override fun getDasboardWeek(week:Int,year:Int):Flow<List<DashboardWeek>>{
        return db.getRegistroDao().getDasboardWeek(week,year)
    }

    override fun getDasboardMonth(month: Int, year: Int): Flow<List<DashboardWeek>> {
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(year, month) to getEndOfMonthTimestamp(year, month)
        return db.getRegistroDao().getDasboardMonth(startTimestamp, endTimestamp)
    }

}