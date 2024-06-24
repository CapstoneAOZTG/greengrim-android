package com.aoztg.greengrim.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.presentation.ui.nft.exchange.detail.ExchangeState
import com.aoztg.greengrim.presentation.ui.splash.SplashActivity
import com.aoztg.greengrim.presentation.util.AppState
import com.aoztg.greengrim.presentation.util.PushUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var keyDataStoreManager: KeyDataStoreManager

    override fun onCreate() {
        super.onCreate()
        PushUtils.acquireWakeLock(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        PushUtils.acquireWakeLock(App.context())

        when (message.data["type"]) {

            "TALK" -> {
                if (!AppState.isForeground) {
                    val nickName = message.data["nickName"]
                    val talk = message.data["message"]
                    val senderId = message.data["senderId"]?.toLong()
                    val memberId: Long? = runBlocking {
                        keyDataStoreManager.getMemberId()
                    }

                    if (senderId != memberId) {
                        sendChatNotification(nickName, talk)
                    }
                }
            }

            "POINT" -> {
                val msg = message.data["roomId"]
                sendPointKeywordNotification("포인트 획득!", msg.toString())
            }

            "EXCHANGE_SUC" -> {
                Log.d("fcm", "exchange success")
                ExchangeState.exchangeSuccess()
            }

            "EXCHANGE_FAIL" -> {
                Log.d("fcm", "exchange failure")
                ExchangeState.exchangeFailure()
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
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_greengrim_logo))
            setSmallIcon(R.mipmap.ic_greengrim_logo)
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
            setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_greengrim_logo))
            setSmallIcon(R.mipmap.ic_greengrim_logo)
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