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
            "WHERE TB_REGISTRO.IS_DEPESA = 1 "+
            "GROUP BY TB_CATEGORIA.ID")
    fun getAllWithTotal():Flow<List<Categoria>>
    @Query("SELECT TB_CATEGORIA.*,SUM(TB_REGISTRO.VALUE) AS TOTAL " +
            "FROM TB_CATEGORIA " +
            "JOIN TB_REGISTRO " +
            "ON TB_REGISTRO.CATEGORIA_FK = TB_CATEGORIA.ID " +
            "WHERE TB_REGISTRO.IS_DEPESA = 1 "+
            "AND CAST(strftime('%m', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :mes " +
            "AND CAST(strftime('%Y', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :ano "+
            "GROUP BY TB_CATEGORIA.ID ")
    fun getAllWithMesAno(mes:Int, ano:Int):Flow<List<Categoria>>
    @Update
    fun update(it: Categoria)
    @Query("DELETE FROM TB_CATEGORIA WHERE ID IN(:it)")
    fun deleteAll(it: List<Int>)
}