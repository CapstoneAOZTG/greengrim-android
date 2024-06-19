package com.aoztg.greengrim.presentation.chatmanager

import android.annotation.SuppressLint
import android.util.Log
import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.presentation.util.Constants.TAG
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

class ChatSocket(
    private val acceptChat: (String) -> Unit,
    private val showToastMessage: (String) -> Unit,
    private val showSnackMessage: (String) -> Unit,
    private val keyDataStoreManager: KeyDataStoreManager,
    private val reConnect: () -> Unit
) {
    companion object {
        const val FOREGROUND = 0
        const val BACKGROUND = 1
    }

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.SOCKET_URL)
    private var applicationState = FOREGROUND

    @SuppressLint("CheckResult")
    fun connectServer() {
        try {
            val headerList = arrayListOf<StompHeader>()
            val jwt = runBlocking {
                keyDataStoreManager.getAccessToken()
            }

            jwt?.let {
                headerList.add(StompHeader("token", it))
            } ?: run {

            }

            stompClient.connect(headerList)

            stompClient.lifecycle().subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.CLOSED -> {
                        if (applicationState == FOREGROUND) {
                            reConnect()
                        }
                    }

                    else -> {}
                }
            }
        } catch (e: Exception) {
            showSnackMessage(e.message.toString())
        }
    }

    fun disconnectServer() {
        stompClient.disconnect()
    }

    fun setApplicationState(state: Int) {
        applicationState = state
    }

    @SuppressLint("CheckResult")
    fun subscribeChat(chatId: Long) {
        try {
            stompClient.topic("/sub/chat/room/$chatId").subscribe { topicMessage ->
                acceptChat(topicMessage.payload)
            }
        } catch (e: Exception) {
            showSnackMessage(e.message.toString())
        }
    }

    @SuppressLint("CheckResult")
    fun subscribeNewChat(chatId: Long) {
        try {
            stompClient.topic("/sub/chat/room/$chatId").subscribe { topicMessage -> }
        } catch (e: Exception) {
            showSnackMessage(e.message.toString())
        }
    }

    fun sendMessage(memberId: Long, chatId: Long, message: String, isChild: Boolean) {
        try {
            val data = JSONObject()
            data.put("senderId", memberId)
            data.put("type", "TALK")
            data.put("roomId", chatId)
            data.put("message", message)
            data.put("isChild", isChild)
            stompClient.send("/pub/chat/message", data.toString()).subscribe()
        } catch (e: Exception) {
            showSnackMessage(e.message.toString())
        }
    }

    fun sendCertification(
        memberId: Long,
        chatId: Long,
        message: String,
        certId: Long,
        certImg: String,
    ) {
        try {
            val data = JSONObject()
            data.put("senderId", memberId)
            data.put("type", "CERT")
            data.put("roomId", chatId)
            data.put("message", message)
            data.put("certId", certId)
            data.put("certImg", certImg)
            data.put("isChild", true)
            stompClient.send("/pub/chat/message", data.toString()).subscribe()
        } catch (e: Exception) {
            showSnackMessage(e.message.toString())
        }
    }
}



