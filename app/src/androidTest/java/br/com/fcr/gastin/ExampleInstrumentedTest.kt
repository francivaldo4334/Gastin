package br.com.fcr.gastin

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.ui.utils.toJson

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("br.com.fcr.gastin", appContext.packageName)
        val model = Registro(
            Value = 1,
            Description = "teste"
        )
        Log.d("TESTE", toJson(model))
    }
}