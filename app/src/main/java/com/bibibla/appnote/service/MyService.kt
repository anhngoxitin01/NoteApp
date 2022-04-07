package com.bibibla.appnote.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bibibla.appnote.R
import com.bibibla.appnote.broadcast.NotificationReceiver
import com.bibibla.appnote.model.MConst

class MyService : Service() {

    private val notificationReceiver = NotificationReceiver()
    private val binder = MyBinder()

    override fun onCreate() {
//        //create intentFilter here
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(MConst.INTENT_NOTIFICATION_CUSTOM)
//
//        //use localBroadcastManager to register it
//        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver , intentFilter)
        super.onCreate()
    }

    override fun onDestroy() {
//        unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //start the task
        Log.d("check", "start the task in service")
        runForeground()
        return START_STICKY
    }

    private fun runForeground() {
        pushNotification()
    }

    fun pushNotification() {
        // create notification
        Log.d("checkBroadcast", "call notification")
//        val builder = Notification.Builder(this)
//        builder.setContentText("This is my description note in here")
//        builder.setContentTitle("Note")
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel("id_channel_notification" , "name_channel_notification" , NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//            builder.setChannelId(channel.id)
//        } else {
//            Log.d("check" , "Loi trong notification")
//        }
//
//        val notification = builder.build()

        val builder = Notification.Builder(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("id_channel_notification" , "name_channel_notification" , NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        } else {
            Log.d("check" , "Loi trong notification")
        }
        //start foreground
        startForeground(1 , builder.build())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class MyBinder : Binder()
    {
        fun getMyService() : MyService{
            return this@MyService
        }
    }
}