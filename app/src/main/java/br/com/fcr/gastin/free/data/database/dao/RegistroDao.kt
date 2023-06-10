package br.com.fcr.gastin.free.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fcr.gastin.free.data.database.model.Registro
import br.com.fcr.gastin.free.data.database.viewmodel.DashboardWeek
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
    @Query("SELECT SUM(VALUE) FROM TB_REGISTRO WHERE IS_DEPESA = 1 " +
            "AND CAST(strftime('%m', datetime(CREATE_AT/1000, 'unixepoch')) AS int) = :mes " +
            "AND CAST(strftime('%Y', datetime(CREATE_AT/1000, 'unixepoch')) AS int) = :ano")
    fun getAllDespesasValorMesAno(mes:Int, ano:Int): Flow<Int?>
    @Query("SELECT SUM(VALUE) FROM TB_REGISTRO WHERE IS_DEPESA = 0 " +
            "AND CAST(strftime('%m', datetime(CREATE_AT/1000, 'unixepoch')) AS int) = :mes " +
            "AND CAST(strftime('%Y', datetime(CREATE_AT/1000, 'unixepoch')) AS int) = :ano")
    fun getAllReceitasValorMesAno(mes:Int, ano:Int): Flow<Int?>
    @Update
    fun update(resgister: Registro)
    @Query("SELECT * FROM TB_REGISTRO WHERE IS_DEPESA = 1 " +
            "AND CAST(strftime('%m', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :mes " +
            "AND CAST(strftime('%Y', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :ano")
    fun getAllDespesasMesAno(mes: Int, ano: Int): Flow<List<Registro>>
    @Query("SELECT * FROM TB_REGISTRO WHERE IS_DEPESA = 0 " +
            "AND CAST(strftime('%m', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :mes " +
            "AND CAST(strftime('%Y', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :ano")
    fun getAllReceitasMesAno(mes: Int, ano: Int): Flow<List<Registro>>
    @Query("SELECT " +
            "    SUM(TB_REGISTRO.VALUE) AS valor, " +
            "    TB_REGISTRO.CREATE_AT AS date " +
            "FROM " +
            "    TB_REGISTRO " +
            "WHERE " +
            "    TB_REGISTRO.IS_DEPESA = 1 " +
            "    AND CAST(strftime('%W', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :week " +
            "    AND CAST(strftime('%Y', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :year " +
            "GROUP BY " +
            "    CAST(strftime('%d', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int)")
    fun getDasboardWeek(week:Int,year:Int):Flow<List<DashboardWeek>>
    @Query("SELECT " +
            "TB_REGISTRO.VALUE AS valor," +
            "TB_REGISTRO.CREATE_AT AS date " +
            "FROM TB_REGISTRO " +
            "WHERE TB_REGISTRO.IS_DEPESA = 1 " +
            "AND CAST(strftime('%m', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :month "+
            "AND CAST(strftime('%Y', datetime(TB_REGISTRO.CREATE_AT/1000, 'unixepoch')) AS int) = :year ")
    fun getDasboardMonth(month: Int, year: Int): Flow<List<DashboardWeek>>
}