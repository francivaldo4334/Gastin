package br.com.fcr.gastin.data.dao

import androidx.room.*
import br.com.fcr.gastin.data.model.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM TB_CATEGORIA WHERE ID = :ID")
    fun getById(ID:Int):Flow<Categoria?>
    @Query("SELECT * FROM TB_CATEGORIA")
    fun getAll(): Flow<List<Categoria>>
    @Insert
    fun insert(Categoria:Categoria):Long
    @Query("DELETE FROM TB_CATEGORIA WHERE ID = :ID")
    fun delete(ID:Int)
    @Query("SELECT TB_CATEGORIA.ID,TB_CATEGORIA.NAME,TB_CATEGORIA.DESCRIPTION,TB_CATEGORIA.COLOR,TB_CATEGORIA.CREATE_AT,SUM(TB_REGISTRO.VALUE) AS TOTAL FROM TB_REGISTRO JOIN TB_CATEGORIA ON TB_CATEGORIA.ID = TB_REGISTRO.CATEGORIA_FK GROUP BY TB_REGISTRO.ID")
    fun getAllWithTotal():Flow<List<Categoria>>
    @Update
    fun update(it: Categoria)
}