package com.aoztg.greengrim.presentation.chatmanager

import android.annotation.SuppressLint
import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.presentation.util.Constants
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.StompHeader

class ChatSocket(
    private val acceptChat: (String) -> Unit,
    private val showToastMessage: (String) -> Unit,
    private val showSnackMessage: (String) -> Unit
) {

    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.SOCKET_URL)

    fun connectServer() {
        try{
            val headerList = arrayListOf<StompHeader>()
            val jwt = App.sharedPreferences.getString(Constants.X_ACCESS_TOKEN, null)

            jwt?.let {
                headerList.add(StompHeader("token", it))
            } ?: run {

            }

            stompClient.connect(headerList)
        } catch(e: Exception){
            showSnackMessage(e.message.toString())
        }
    }

    fun disconnectServer(){
        stompClient.disconnect()
    }


    @SuppressLint("CheckResult")
    fun subscribeChat(chatId: Long) {
        try{
            stompClient.topic("/sub/chat/room/$chatId").subscribe { topicMessage ->
                acceptChat(topicMessage.payload)
            }

        } catch(e: Exception){
            showSnackMessage(e.message.toString())
        }
    }

    @SuppressLint("CheckResult")
    fun subscribeNewChat(chatId: Long){
        try{
            stompClient.topic("/sub/chat/room/$chatId").subscribe { topicMessage -> }
        } catch(e: Exception){
            showSnackMessage(e.message.toString())
        }
    }

    fun sendMessage(memberId: Long, chatId: Long, message: String) {
        try{
            val data = JSONObject()
            data.put("senderId", memberId)
            data.put("type", "TALK")
            data.put("roomId", chatId)
            data.put("message", message)
            stompClient.send("/pub/chat/message", data.toString()).subscribe()
        } catch(e: Exception){
            showSnackMessage(e.message.toString())
        }
    }

    fun sendCertification(memberId: Long, chatId: Long, message: String, certId: Long, certImg: String, ) {
        try{
            val data = JSONObject()
            data.put("senderId", memberId)
            data.put("type", "CERT")
            data.put("roomId", chatId)
            data.put("message", message)
            data.put("certId", certId)
            data.put("certImg", certImg)
            stompClient.send("/pub/chat/message", data.toString()).subscribe()
        } catch(e: Exception){
            showSnackMessage(e.message.toString())
        }
    }
}



