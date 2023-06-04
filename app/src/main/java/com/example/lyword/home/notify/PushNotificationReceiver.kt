package com.example.lyword.home.notify

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lyword.MainActivity
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.NotifyEntity
import com.example.lyword.setAlarm
import com.google.api.client.util.DateTime
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.Date

class PushNotificationReceiver : BroadcastReceiver() {

    private val channelId = "lyword_notify_id"
    lateinit var db : LywordDatabase

    private val format = SimpleDateFormat("yyyy.MM.dd")

    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationChannel(context)
        Log.d("NOTIFY_RECEIVER", "Do alarm")

        db = LywordDatabase.getInstance(context!!)

        val notificationId = intent!!.getIntExtra("notification_id", 0)
        val notificationTitle = intent.getStringExtra("notification_title")
        val notificationContent = intent.getStringExtra("notification_content")

        val builder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.ic_logo_splash)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val clickIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("NOTIFY_RECEIVER", "No permission")
            return
        }
        notificationManager.notify(notificationId, builder.build())
        setAlarm(context)

        val thread = Thread {
            val id = db.notifyDao.insertNotify(
                NotifyEntity(
                    0,
                    notificationTitle!!,
                    notificationContent!!,
                    null,
                    0,
                    format.format(System.currentTimeMillis())
                )
            )
            Log.d("NOTIFY_RECEIVER", "Input DB : ${id}")
        }
        thread.start()
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Lyword Notify"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            channel.setSound(soundUri, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build())

            val pattern = longArrayOf(0,500,200,500)
            channel.vibrationPattern = pattern
            channel.enableVibration(true)

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}