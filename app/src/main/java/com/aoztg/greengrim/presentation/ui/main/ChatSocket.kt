package com.aoztg.greengrim.presentation.ui.main

import android.annotation.SuppressLint
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.presentation.util.Constants
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.StompHeader

class ChatSocket(
    private val acceptChat: (String) -> Unit
) {

    private val url = "wss://dev.greengrim.store/ws"
    private val jwt = App.sharedPreferences.getString(Constants.X_ACCESS_TOKEN, null)
    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    init {
        connectServer()
    }

    private fun connectServer() {

        val headerList = arrayListOf<StompHeader>()

        jwt?.let {
            headerList.add(StompHeader("token", it))
        } ?: run {

        }

        stompClient.connect(headerList)
    }


    @SuppressLint("CheckResult")
    fun subscribeChat(chatId: Int) {
        stompClient.topic("/sub/chat/room/$chatId").subscribe { topicMessage ->
            acceptChat(topicMessage.payload)
        }
    }

    fun sendMessage(memberId: Long, chatId: Int, message: String) {
        val data = JSONObject()
        data.put("senderId", memberId)
        data.put("type", "TALK")
        data.put("roomId", chatId)
        data.put("message", message)
        stompClient.send("/pub/chat/message", data.toString()).subscribe()
    }

    fun sendCertification(memberId: Long, chatId: Int, message: String, certId: Int, certImg: String, ) {
        val data = JSONObject()
        data.put("senderId", memberId)
        data.put("type", "CERT")
        data.put("roomId", chatId)
        data.put("message", message)
        data.put("certId", certId)
        data.put("certImg", certImg)
        stompClient.send("/pub/chat/message", data.toString()).subscribe()
    }
}



