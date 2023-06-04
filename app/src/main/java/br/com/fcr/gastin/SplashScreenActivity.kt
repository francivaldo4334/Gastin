package br.com.fcr.gastin

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
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
import br.com.fcr.gastin.data.notification.NotificationReceiver
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.SplashScreenPage
import br.com.fcr.gastin.ui.theme.GastinTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class SplashScreenActivity : ComponentActivity() {
    fun scheduleNotification(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationsTime = listOf(12,21)
        for (time in notificationsTime){
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY,time)
                set(Calendar.MINUTE,0)
                set(Calendar.SECOND,0)
            }
            val intent = Intent(this,NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this,time,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
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
        val isFirstTime = sharedPreferences.getBoolean(Constants.IS_FIRST_TIME,true)
        if(isFirstTime) {
            scheduleNotification()
            editor.putBoolean(Constants.IS_FIRST_TIME,false)
            editor.apply()
        }
    }
}