package com.bibibla.appnote.broadcast

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bibibla.appnote.R
import com.bibibla.appnote.model.MConst
import android.app.NotificationManager
import android.os.Build
import com.bibibla.appnote.model.Note


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action.equals(MConst.NOTIFICATION_CUSTOM)) {
            Log.d("check" , "Log in broadcast Receiver")

            //get data from intent
            val title = intent?.extras?.getString("title") as String
            val description = intent?.extras?.getString("description") as String

            //cacul time for push value
            createNotification(context, title , description)
        }
        //TODO("Not yet implemented")
        //display notification when get time
    }

    private fun createNotification(context: Context?, title:String , description:String) {
        Log.d("checkBroadcast", "call notification")
        val builder = Notification.Builder(context)
        builder.setContentText(description)
        builder.setContentTitle(title)
        builder.setSmallIcon(R.mipmap.ic_launcher)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setTimeoutAfter(time * 1000)
//        }

        val notificationManager =  context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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