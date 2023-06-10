package br.com.fcr.gastin.free.data.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import br.com.fcr.gastin.free.HomeActivity
import br.com.fcr.gastin.free.R
import br.com.fcr.gastin.free.ui.common.Constants

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val message = context.getString(R.string.txt_hora_de_anotar_seus_gastos)

        val intent = Intent(context, HomeActivity::class.java).putExtra(Constants.OPEN_REGISTRO,true)
        val pendingIntent = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, HomeActivity.CHANNEL_ID)
            .setContentTitle(context.getString(R.string.txt_atualizar_registros))
            .setContentText(message)
            .setSmallIcon(R.drawable.logo_app)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(HomeActivity.NOTIFICATION_ID,notification)
    }
}