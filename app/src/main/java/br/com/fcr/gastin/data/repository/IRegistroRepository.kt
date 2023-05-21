package br.com.fcr.gastin.data.repository

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.model.Registro

interface IRegistroRepository {
    fun getAllDespesas(): LiveData<List<Registro>>
    fun getAllReceitas(): LiveData<List<Registro>>
    fun getById(ID:Int): Registro
    fun delete(ID:Int)
    fun insert(it: Registro)
    fun getall(): LiveData<List<Registro>>
    fun getRegistrosByCategoriaId(id: Int): Int
}