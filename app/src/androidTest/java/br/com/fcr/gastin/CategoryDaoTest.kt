package br.com.fcr.gastin

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.fcr.gastin.data.database.MyDatabase
import br.com.fcr.gastin.data.database.dao.CategoriaDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {
    private lateinit var db: MyDatabase
    private lateinit var dao: CategoriaDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MyDatabase::class.java).build()
        dao = db.getCategoriaDao()
    }
    @After
    fun dispose() {
        db.close()
    }
}