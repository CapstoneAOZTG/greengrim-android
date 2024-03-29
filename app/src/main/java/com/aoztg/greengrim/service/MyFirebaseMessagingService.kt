package com.aoztg.greengrim.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.presentation.ui.splash.SplashActivity
import com.aoztg.greengrim.presentation.util.PushUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        PushUtils.acquireWakeLock(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        PushUtils.acquireWakeLock(App.context())
        //수신한 메시지를 처리

        when (message.data["type"]) {

            "TALK" -> {
                val nickName = message.data["nickName"]
                val talk = message.data["message"]
                sendChatNotification(nickName, talk)
            }

            "POINT" -> {
                val msg = message.data["roomId"]
                sendPointKeywordNotification("포인트 획득!", msg.toString())
            }

            "KEYWORD" -> {
                val msg = message.data["roomId"]
                sendPointKeywordNotification("키워드 획득!", msg.toString())
            }
        }
    }

    private fun sendChatNotification(sender: String? = "", message: String? = "") {

        val uniId = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            uniId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "greengrim"

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            priority = NotificationCompat.PRIORITY_HIGH
            setContentTitle(sender)
            setContentText(message)
            setContentIntent(pIntent)
            setAutoCancel(true)
            color = Color.argb(1, 120, 63, 59)
            setColorized(true)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_app_logo))
            setSmallIcon(R.mipmap.ic_app_logo)
        }

        // Head up 알람 설정
        notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setFullScreenIntent(pIntent, true)

        getSystemService(NotificationManager::class.java).run {
            val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
            createNotificationChannel(channel)

            notify(uniId, notificationBuilder.build())
        }
    }

    private fun sendPointKeywordNotification(title: String, message: String) {

        val uniId = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            uniId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "greengrim"

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            priority = NotificationCompat.PRIORITY_HIGH
            setContentTitle(title)
            setContentText(message)
            setContentIntent(pIntent)
            setAutoCancel(true)
            color = Color.argb(1, 120, 63, 59)
            setColorized(true)
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_app_logo))
            setSmallIcon(R.mipmap.ic_app_logo)
        }

        // Head up 알람 설정
        notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setFullScreenIntent(pIntent, true)

        getSystemService(NotificationManager::class.java).run {
            val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
            createNotificationChannel(channel)

            notify(uniId, notificationBuilder.build())
        }
    }

    suspend fun getFirebaseToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                continuation.resume(token)
            } else {
                continuation.resumeWithException(
                    task.exception ?: RuntimeException("Error getting FCM token")
                )
            }
        }
    }

}