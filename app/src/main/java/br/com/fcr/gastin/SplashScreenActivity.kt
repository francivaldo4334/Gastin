package br.com.fcr.gastin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.SplashScreenPage
import br.com.fcr.gastin.ui.theme.GastinTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
        setContent {
            GastinTheme(IsDarkTheme) {//Gestao de gasto
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SplashScreenPage(IsDarkTheme)
                }
            }
        }
        lifecycleScope.launch(Dispatchers.Main){
            delay(3000)
            startActivity(Intent(this@SplashScreenActivity,HomeActivity::class.java))
            finish()
        }
    }
}