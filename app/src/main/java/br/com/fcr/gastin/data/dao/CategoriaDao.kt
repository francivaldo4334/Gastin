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
    @Query("SELECT TB_CATEGORIA.*,SUM(TB_REGISTRO.VALUE) AS TOTAL " +
            "FROM TB_CATEGORIA " +
            "JOIN TB_REGISTRO " +
            "ON  TB_REGISTRO.CATEGORIA_FK = TB_CATEGORIA.ID " +
            "GROUP BY TB_CATEGORIA.ID")
    fun getAllWithTotal():Flow<List<Categoria>>
    @Update
    fun update(it: Categoria)
    @Query("DELETE FROM TB_CATEGORIA WHERE ID IN(:it)")
    fun deleteAll(it: List<Int>)
}