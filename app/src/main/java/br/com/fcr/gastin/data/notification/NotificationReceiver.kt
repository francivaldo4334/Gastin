package br.com.fcr.gastin.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val message = "Hora de anotar seus gastos!"
        val channelName = "Lembrete"
        val channelId = context.packageName+channelName
        val notificationId = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val activityIntent = Intent(context,HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,1,activityIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context,channelId)
            .setContentTitle("Atualizar Registros")
            .setContentText(message)
            .setSmallIcon(R.drawable.logo_app)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId,notification)
    }
}