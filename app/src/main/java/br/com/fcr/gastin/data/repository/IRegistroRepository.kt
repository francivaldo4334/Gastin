package br.com.fcr.gastin.data.repository

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.model.Registro
import kotlinx.coroutines.flow.Flow

interface IRegistroRepository {
    fun getAllDespesas(): Flow<List<Registro>>
    fun getAllDespesasMesAno(mes:Int,ano:Int): Flow<List<Registro>>
    fun getAllReceitas(): Flow<List<Registro>>
    fun getAllReceitasMesAno(mes:Int,ano:Int): Flow<List<Registro>>
    fun getAllDespesasValor(): Flow<Int?>
    fun getAllReceitasValor(): Flow<Int?>
    fun getAllDespesasValorMesAno(mes:Int,ano:Int): Flow<Int?>
    fun getAllReceitasValorMesAno(mes:Int,ano:Int): Flow<Int?>
    fun getById(ID:Int): Flow<Registro>
    fun delete(ID:Int)
    fun insert(it: Registro)
    fun getall(): LiveData<List<Registro>>
    fun getRegistrosByCategoriaId(id: Int): Flow<Int>
    fun update(resgister: Registro)
    fun deleteAll(ids: List<Int>)
}