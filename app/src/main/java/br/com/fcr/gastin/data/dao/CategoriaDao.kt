package br.com.fcr.gastin.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.fcr.gastin.data.model.Categoria

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM TB_CATEGORIA WHERE ID = :ID")
    fun getById(ID:Int):Categoria?
    @Query("SELECT * FROM TB_CATEGORIA")
    fun getAll():List<Categoria>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(Categoria:Categoria):Long
    @Query("DELETE FROM TB_CATEGORIA WHERE ID = :ID")
    fun delete(ID:Int)
}