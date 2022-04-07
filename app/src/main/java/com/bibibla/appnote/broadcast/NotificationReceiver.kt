package com.bibibla.appnote.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.util.Log
import com.bibibla.appnote.R
import com.bibibla.appnote.model.MConst

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action.equals(MConst.INTENT_NOTIFICATION_CUSTOM)) {
            Log.d("check" , "running broadcast receiver")
//            pushNotification(context)
        }
    }

    fun pushNotification(context: Context?) {
        Log.d("checkBroadcast", "call notification")
        val builder = Notification.Builder(context)
        builder.setContentText("This is my description note in here")
        builder.setContentTitle("Note")
        builder.setSmallIcon(R.mipmap.ic_launcher)

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("id_channel_notification" , "name_channel_notification" , NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        } else {
            Log.d("check" , "Loi trong notification")
        }

        notificationManager.notify(1 , builder.build())
    }
}
