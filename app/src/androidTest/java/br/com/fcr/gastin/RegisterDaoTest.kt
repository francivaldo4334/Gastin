package br.com.fcr.gastin

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.fcr.gastin.data.database.MyDatabase
import br.com.fcr.gastin.data.database.dao.RegistroDao
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.data.database.resource.getEndOfMonthTimestamp
import br.com.fcr.gastin.data.database.resource.getStartOfMonthTimestamp
import br.com.fcr.gastin.data.database.resource.getStartOfWeekTimestamp
import br.com.fcr.gastin.data.database.resource.getEndOfWeekTimestamp
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class RegisterDaoTest {
    private lateinit var db: MyDatabase
    private lateinit var dao: RegistroDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase::class.java).build()
        dao = db.getRegistroDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() = runBlocking{
        val register = Registro(Description = "TESTE")
        dao.insert(register)
        val job = async {
            dao.getById(1).first()
        }
        val fetchedUser = job.await()
        assertEquals(fetchedUser.Description, register.Description)
    }

    @Test
    fun getAllDespesasValorMesAno() = runBlocking{
        val calendar = Calendar.getInstance()
        calendar.set(2024, 1, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 2, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 2, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = false,

            ).apply {
                calendar.set(2024, 2, 1)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                startDate = calendar.time
                calendar.set(2024, 3, 1)
                endDate = calendar.time
            }
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = false,
            ).apply {
                calendar.set(2024, 3, 1)
                startDate = calendar.time
                calendar.set(2024, 4, 1)
                endDate = calendar.time
            }
        )
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(2024, 1) to getEndOfMonthTimestamp(2024, 1)
        val job = async { dao.getAllDespesasValorMesAno(startTimestamp, endTimestamp).first()}
        val value = job.await()
        assertEquals(value ,4)
    }

    @Test
    fun getAllDespesasMesAno() = runBlocking{
        val calendar = Calendar.getInstance()
        calendar.set(2024, 1, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 2, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 2, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = false,

                ).apply {
                calendar.set(2024, 2, 1)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                startDate = calendar.time
                calendar.set(2024, 3, 1)
                endDate = calendar.time
            }
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = true,
                isRecurrent = true,
                isEverDays = false,
            ).apply {
                calendar.set(2024, 3, 1)
                startDate = calendar.time
                calendar.set(2024, 4, 1)
                endDate = calendar.time
            }
        )
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(2024, 1) to getEndOfMonthTimestamp(2024, 1)
        val job = async { dao.getAllDespesasMesAno(startTimestamp, endTimestamp).first()}
        val value = job.await()
        assertEquals(value.count() ,4)
    }

    @Test
    fun getAllReceitasValorMesAno() = runBlocking{
        val calendar = Calendar.getInstance()
        calendar.set(2024, 1, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 2, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 2, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false,

            ).apply {
                calendar.set(2024, 2, 1)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                startDate = calendar.time
                calendar.set(2024, 3, 1)
                endDate = calendar.time
            }
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false,
            ).apply {
                calendar.set(2024, 3, 1)
                startDate = calendar.time
                calendar.set(2024, 4, 1)
                endDate = calendar.time
            }
        )
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(2024, 1) to getEndOfMonthTimestamp(2024, 1)
        val job = async { dao.getAllReceitasValorMesAno(startTimestamp, endTimestamp).first()}
        val value = job.await()
        assertEquals(value ,4)
    }

    @Test
    fun getAllReceitasMesAno() = runBlocking{
        val calendar = Calendar.getInstance()
        calendar.set(2024, 1, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 2, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 2, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = true
            )
        )
        calendar.set(2024, 10, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false,

                ).apply {
                calendar.set(2024, 2, 1)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                startDate = calendar.time
                calendar.set(2024, 3, 1)
                endDate = calendar.time
            }
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false,
            ).apply {
                calendar.set(2024, 3, 1)
                startDate = calendar.time
                calendar.set(2024, 4, 1)
                endDate = calendar.time
            }
        )
        val (startTimestamp, endTimestamp) = getStartOfMonthTimestamp(2024, 1) to getEndOfMonthTimestamp(2024, 1)
        val job = async { dao.getAllReceitasMesAno(startTimestamp, endTimestamp).first()}
        val value = job.await()
        assertEquals(value.count() ,4)
    }

    fun getDasboardWeek() = runBlocking {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = getStartOfWeekTimestamp(2024, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.timeInMillis = getEndOfWeekTimestamp(2024, 1)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        calendar.timeInMillis = getEndOfWeekTimestamp(2024, 4)
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false
            )
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = true
            )
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false
            ).apply {
                calendar.timeInMillis = getEndOfWeekTimestamp(2024, 1)
                startDate = calendar.time
                calendar.timeInMillis = getEndOfWeekTimestamp(2024, 4)
            }
        )
        dao.insert(
            Registro(
                Value = 1,
                CreateAT = calendar.time,
                IsDespesa = false,
                isRecurrent = true,
                isEverDays = false
            ).apply {
                calendar.timeInMillis = getEndOfWeekTimestamp(2024, 2)
                startDate = calendar.time
                calendar.timeInMillis = getEndOfWeekTimestamp(2024, 4)
            }
        )
        val job = async { dao.getDasboardWeek(1, 2024).first()}
        val value = job.await()
        assertEquals(value.count() ,4)
    }
}