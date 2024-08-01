package br.com.fcr.gastin.data.database.dao

import androidx.room.*
import br.com.fcr.gastin.data.database.model.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM TB_CATEGORIA WHERE ID = :ID")
    fun getById(ID:Int):Flow<Categoria?>
    @Query("SELECT * FROM TB_CATEGORIA")
    fun getAll(): Flow<List<Categoria>>
    @Insert
    fun insert(Categoria: Categoria):Long
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
            "AND (TB_REGISTRO.CREATE_AT BETWEEN :startTimestamp AND :endTimestamp " +
            """
            OR (TB_REGISTRO.IS_RECURRENT = 1 AND TB_REGISTRO.IS_EVER_DAYS = 1)
            OR (
                TB_REGISTRO.IS_RECURRENT = 1
                AND TB_REGISTRO.START_DATE <= :endTimestamp
                AND TB_REGISTRO.END_DATE >= :startTimestamp
            ))""" +
            "GROUP BY TB_CATEGORIA.ID ")
    fun getAllWithMesAno(startTimestamp: Long, endTimestamp: Long):Flow<List<Categoria>>
    @Update
    fun update(it: Categoria)
    @Query("DELETE FROM TB_CATEGORIA WHERE ID IN(:it)")
    fun deleteAll(it: List<Int>)
}