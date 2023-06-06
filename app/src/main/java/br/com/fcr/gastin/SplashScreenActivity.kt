package br.com.fcr.gastin

import android.Manifest
import android.R
import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
    companion object{
        var CHANNEL_ID = "channel_notification_Gastin_ID"
    }
    fun scheduleNotification(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar21 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,12)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }
        val intent21 = Intent(applicationContext,NotificationReceiver::class.java)
        val pendingIntent21 = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent21,
            PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar21.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent21
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
        val firstTime = sharedPreferences.getBoolean(Constants.IS_FIRST_TIME,true)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            if(firstTime) {
                scheduleNotification()
                edit.putBoolean(Constants.IS_FIRST_TIME,false)
                edit.apply()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val name = "canal Gastin"
        val desc = "canal de notificacao Gastin"
        val important = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID,name,important)
        channel.description = desc
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}