package br.com.fcr.gastin.data.database.repository

import br.com.fcr.gastin.data.database.model.Categoria
import kotlinx.coroutines.flow.Flow

interface ICategoriaRepository {
    fun getAll():Flow<List<Categoria>>
    fun getById(ID:Int):Flow<Categoria?>
    fun insert(it: Categoria)
    fun update(it: Categoria)
    fun delete(ID: Int)
    fun getAllWithTotal():Flow<List<Categoria>>
    fun deleteAll(it: List<Int>)
    fun getAllWithMesAno(mes:Int,ano:Int):Flow<List<Categoria>>
}