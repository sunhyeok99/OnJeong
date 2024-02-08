package com.a503.onjeong.global.firebase


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.a503.onjeong.R
import com.a503.onjeong.domain.videocall.activity.VideoCallActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random


class FirebaseMessagingService : FirebaseMessagingService() {
    private val NOTIFICATION_CHANNEL_ID = "sample.noti.app"
    private val NOTIFICATION_CHANNEL_NAME = "Notification"
    private val NOTIFICATION_CHANNEL_DESCRIPTION = "notification channel"

    override fun onNewToken(token: String) {
        Log.d("FCM Log", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM Log", "Notification Message Received: $remoteMessage")

        if (remoteMessage.data.isNotEmpty()) {
            val content = remoteMessage.data["title"]
            Log.d("FCM Log", "Notification Message content: $content")

            val intent = Intent(this, VideoCallActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("sessionId", content)
            val pendingIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

            showNotification(content, pendingIntent)
        }
    }

    private fun showNotification(content: String?, pendingIntent: PendingIntent) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("A new post you might be interested in.")
                .setContentText(content)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent)
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        var notificationChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel!!.description = NOTIFICATION_CHANNEL_DESCRIPTION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(notificationChannel!!)
        }
    }
}