package br.com.fcr.gastin.data.repository

import androidx.lifecycle.LiveData
import br.com.fcr.gastin.data.model.Categoria

interface ICategoriaRepository {
    fun getAll():LiveData<List<Categoria>>
    fun getById(ID:Int):LiveData<Categoria>
    fun insert(it: Categoria)
    fun delete(ID: Int)
}