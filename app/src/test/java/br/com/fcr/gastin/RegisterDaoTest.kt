package br.com.fcr.gastin

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.fcr.gastin.data.database.MyDatabase
import br.com.fcr.gastin.data.database.dao.RegistroDao
import br.com.fcr.gastin.data.database.model.Registro
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        assert(fetchedUser.Description == register.Description)
    }
}