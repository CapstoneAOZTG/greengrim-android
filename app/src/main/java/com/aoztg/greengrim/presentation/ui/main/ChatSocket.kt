package com.aoztg.greengrim.presentation.ui.main

import android.annotation.SuppressLint
import android.util.Log
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.TAG
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompHeader


//fun connectChat(chatId: Int): WebSocket {
//    val request = Request.Builder()
//        .url("wss://dev.greengrim.store/ws/sub/chat/room/$chatId")
//        .build()
//
//    val listener = object : WebSocketListener() {
//        override fun onOpen(webSocket: WebSocket, response: Response) {
//            super.onOpen(webSocket, response)
//
//            Log.d(TAG, "소켓 연결 성공")
//        }
//
//        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//            super.onMessage(webSocket, bytes)
//
//            Log.d(TAG, bytes.toString())
//        }
//    }
//
//    val client = OkHttpClient()
//    val webSocket = client.newWebSocket(request, listener)
//    return webSocket
//}

class ChatSocket(){

    private val url = "wss://dev.greengrim.store/ws"
    private val jwt = App.sharedPreferences.getString(Constants.X_ACCESS_TOKEN, null)
    private lateinit var stompClient : StompClient

    fun connectChat() {

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
        val headerList = arrayListOf<StompHeader>()

        jwt?.let {
            headerList.add(StompHeader("token", it))
        } ?: run {

        }

        stompClient.connect(headerList)
    }

    @SuppressLint("CheckResult")
    fun subscribeChat(chatId: Int) {

        stompClient.topic("/sub/chat/room/$chatId").subscribe{ topicMessage ->
            Log.d(TAG, topicMessage.payload)
        }
    }

    fun sendMessage(chatId: Int, message: String){

        val data = JSONObject()
        data.put("token", jwt)
        data.put("type","TALK")
        data.put("roomId",chatId)
        data.put("message",message)
        stompClient.send("/pub/chat/message",data.toString()).subscribe()
    }

}



