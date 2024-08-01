package br.com.fcr.gastin.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.data.database.viewmodel.DashboardWeek
import kotlinx.coroutines.flow.Flow
@Dao
interface RegistroDao {
    @Query("SELECT * FROM TB_REGISTRO WHERE ID = :ID")
    fun getById(ID:Int): Flow<Registro>
    @Query("SELECT * FROM TB_REGISTRO WHERE IS_DEPESA = 1")
    fun getAllDespesas(): Flow<List<Registro>>
    @Query("SELECT * FROM TB_REGISTRO WHERE IS_DEPESA = 0")
    fun getAllReceitas(): Flow<List<Registro>>
    @Insert
    fun insert(Categoria: Registro):Long
    @Query("DELETE FROM TB_REGISTRO WHERE ID = :ID")
    fun delete(ID:Int)
    @Query("DELETE FROM TB_REGISTRO WHERE ID IN(:it)")
    fun deleteAll(it: List<Int>)
    @Query("SELECT * FROM TB_REGISTRO")
    fun getAll(): LiveData<List<Registro>>
    @Query("SELECT SUM(VALUE) FROM TB_REGISTRO WHERE CATEGORIA_FK = :id AND IS_DEPESA = 1;")
    fun getRegistrosByCategoriaId(id: Int): Flow<Int>
    @Query("SELECT SUM(VALUE) FROM TB_REGISTRO WHERE IS_DEPESA = 1")
    fun getAllDespesasValor(): Flow<Int?>
    @Query("SELECT SUM(VALUE) FROM TB_REGISTRO WHERE IS_DEPESA = 0")
    fun getAllReceitasValor(): Flow<Int?>
    @Query("""
SELECT 
    SUM(VALUE) 
FROM TB_REGISTRO 
WHERE 
    IS_DEPESA = 1 
    AND (
        (CREATE_AT BETWEEN :startTimestamp AND :endTimestamp)
        OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
        OR (
            IS_RECURRENT = 1
            AND START_DATE <= :endTimestamp
            AND END_DATE >= :startTimestamp
        )
    )
""")
    fun getAllDespesasValorMesAno(startTimestamp: Long, endTimestamp: Long): Flow<Int?>
    @Query("""
SELECT SUM(VALUE) 
FROM TB_REGISTRO 
WHERE 
    IS_DEPESA = 0 
    AND (
        (CREATE_AT BETWEEN :startTimestamp AND :endTimestamp)
        OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
        OR (
            IS_RECURRENT = 1
            AND START_DATE <= :endTimestamp
            AND END_DATE >= :startTimestamp
        )
    )
""")
    fun getAllReceitasValorMesAno(startTimestamp: Long, endTimestamp: Long): Flow<Int?>

    @Update
    fun update(resgister: Registro)
    @Query("""
SELECT * 
FROM TB_REGISTRO 
WHERE 
    IS_DEPESA = 1 
    AND (
        (CREATE_AT BETWEEN :startTimestamp AND :endTimestamp)
        OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
        OR (
            IS_RECURRENT = 1
            AND START_DATE <= :endTimestamp
            AND END_DATE >= :startTimestamp
        )
    )
""")
    fun getAllDespesasMesAno(startTimestamp: Long, endTimestamp: Long): Flow<List<Registro>>
    @Query("""
SELECT * 
FROM TB_REGISTRO 
WHERE 
    IS_DEPESA = 0 
    AND (
        (CREATE_AT BETWEEN :startTimestamp AND :endTimestamp)
        OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
        OR (
            IS_RECURRENT = 1
            AND START_DATE <= :endTimestamp
            AND END_DATE >= :startTimestamp
        )
    )
""")
    fun getAllReceitasMesAno(startTimestamp: Long, endTimestamp: Long): Flow<List<Registro>>
    @Query("""
SELECT 
    SUM(TB_REGISTRO.VALUE) AS valor, 
    TB_REGISTRO.CREATE_AT AS date
FROM 
    TB_REGISTRO 
WHERE 
    TB_REGISTRO.IS_DEPESA = 1 
    AND (
        strftime('%Y/%W', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) = (CAST(:year as TEXT) || '/' || CAST(:week as TEXT))
        OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
        OR (
            IS_RECURRENT = 1
            AND strftime('%Y/%W', datetime(START_DATE/1000, 'unixepoch')) <= (CAST(:year as TEXT) || '/' || CAST(:week as TEXT))
            AND strftime('%Y/%W', datetime(END_DATE/1000, 'unixepoch')) >= (CAST(:year as TEXT) || '/' || CAST(:week as TEXT))
        )
    )
GROUP BY 
    CAST(strftime('%d', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int)
""")
    fun getDasboardWeek(week:Int,year:Int):Flow<List<DashboardWeek>>
    @Query("""
SELECT 
    TB_REGISTRO.VALUE AS valor,
    TB_REGISTRO.CREATE_AT AS date 
FROM TB_REGISTRO 
WHERE
TB_REGISTRO.IS_DEPESA = 1 
AND (
    (CREATE_AT BETWEEN :startTimestamp AND :endTimestamp)
    OR (IS_RECURRENT = 1 AND IS_EVER_DAYS = 1)
    OR (
        IS_RECURRENT = 1
        AND START_DATE <= :endTimestamp
        AND END_DATE >= :startTimestamp
    )
)
""")

    fun getDasboardMonth(startTimestamp: Long, endTimestamp: Long): Flow<List<DashboardWeek>>
}