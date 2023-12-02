package com.aoztg.greengrim.presentation.chatmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.chatmanager.mapper.toChatEntity
import com.aoztg.greengrim.presentation.chatmanager.mapper.toUiUnReadChatData
import com.aoztg.greengrim.presentation.chatmanager.mapper.toUnReadChatEntity
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.chatmanager.model.UiUnReadChatData
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.DATE
import com.aoztg.greengrim.presentation.util.Constants.NOTHING
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ChatEvent {
    data class ShowToastMessage(val msg: String) : ChatEvent()
}

@HiltViewModel
class ChatManager @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _events: MutableSharedFlow<ChatEvent> = MutableSharedFlow()
    val event: SharedFlow<ChatEvent> = _events

    private val _newChat = MutableSharedFlow<ChatMessage>()
    val newChat: SharedFlow<ChatMessage> = _newChat.asSharedFlow()

    private val _firstConnect = MutableStateFlow(false)
    val firstConnect: StateFlow<Boolean> = _firstConnect.asStateFlow()

    private val _initialConnectChatIds = MutableStateFlow<List<Int>>(emptyList())
    val initialConnectChatIds: StateFlow<List<Int>> = _initialConnectChatIds.asStateFlow()

    var unReadChatData = listOf<UiUnReadChatData>()

    private val _updateUnReadChat = MutableSharedFlow<List<UiUnReadChatData>>()
    val updateUnReadChat: SharedFlow<List<UiUnReadChatData>> = _updateUnReadChat.asSharedFlow()

    private val _unReadCnt = MutableStateFlow(0)
    val unReadCnt: StateFlow<Int> = _unReadCnt.asStateFlow()

    private var memberId: Long = 0
    private val chatSocket = ChatSocket(::receiveMessage)

    init {
        setMemberId()
        getMyChatIds()
    }

    private fun setMemberId() {
        val memberId: Long = App.sharedPreferences.getLong(Constants.MEMBER_ID, -1L)
        if (memberId != -1L) {
            this.memberId = memberId
        } else {

        }
    }

    private fun getMyChatIds() {
        viewModelScope.launch {
            chatRepository.getChatRooms().let {
                when (it) {
                    is BaseState.Success -> {
                        _initialConnectChatIds.value = it.body.map { data ->
                            data.chatroomId
                        }

                        chatSocket.connectServer()
                        initialConnectChatIds.value.forEach { chatId ->
                            chatSocket.subscribeChat(chatId)
                        }

                        unReadChatData = initialConnectChatIds.value.map { chatId ->
                            UiUnReadChatData(
                                chatId = chatId
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatEvent.ShowToastMessage(it.msg))
                    }
                }
                _firstConnect.value = true
            }

            getUnReadChat()
        }
    }

    private fun getUnReadChat() {
        viewModelScope.launch {
            when (val response = chatRepository.getUnReadChatData()) {
                is BaseState.Success -> {
                    if (response.body.isNotEmpty()) {
                        response.body.forEach {
                            Log.d(TAG,it.toString())
                        }
                        unReadChatData = response.body.map {
                            it.toUiUnReadChatData()
                        }
                        _unReadCnt.value =
                            response.body.map { it.unReadCount }
                                .reduce { total, num -> total + num }
                    }
                }

                is BaseState.Error -> {
                    Log.d(Constants.TAG, response.msg)
                }
            }
        }
    }

    fun subscribeNewChat(chatId: Int) {
        initialConnectChatIds.value.forEach {
            if (chatId == it) {
                unReadChatData = unReadChatData + UiUnReadChatData(chatId = chatId)
                return
            }
        }
        chatSocket.subscribeChat(chatId)
        _initialConnectChatIds.value = initialConnectChatIds.value + chatId
        unReadChatData = unReadChatData + UiUnReadChatData(chatId = chatId)
    }

    fun sendMessage(chatId: Int, message: String) {
        chatSocket.sendMessage(
            memberId,
            chatId,
            message
        )
    }

    fun sendCertificationMessage(chatId: Int, message: String, certId: Int, certImg: String) {
        chatSocket.sendCertification(
            memberId,
            chatId,
            message,
            certId,
            certImg
        )
    }

    private fun receiveMessage(payload: String) {
        val chatMessage = Gson().fromJson(payload, ChatMessage::class.java)
        viewModelScope.launch {
            _newChat.emit(chatMessage)
        }
        updateUnReadChatData(chatMessage)
        storeChatMessage(chatMessage)
    }

    private fun updateUnReadChatData(chatMessage: ChatMessage) {
        // todo 메세지 수신시, recentChatData 업데이트
        if (chatMessage.type == "TALK" || chatMessage.type == "CERT") {
            if (chatMessage.senderId == memberId) {
                unReadChatData = unReadChatData.map {
                    if (it.chatId == chatMessage.roomId) {
                        it.copy(
                            recentChat = chatMessage.message,
                            recentChatTime = chatMessage.sentTime,
                            recentChatDate = chatMessage.sentDate,
                            unReadCount = it.unReadCount
                        )
                    } else {
                        it
                    }
                }
            } else {
                unReadChatData = unReadChatData.map {
                    if (it.chatId == chatMessage.roomId) {
                        it.copy(
                            recentChat = chatMessage.message,
                            recentChatTime = chatMessage.sentTime,
                            recentChatDate = chatMessage.sentDate,
                            unReadCount = it.unReadCount + 1
                        )
                    } else {
                        it
                    }
                }

                _unReadCnt.value = unReadCnt.value + 1
            }

            viewModelScope.launch {
                _updateUnReadChat.emit(unReadChatData)
            }
            storeUnReadChat()
        }
    }

    private fun storeChatMessage(message: ChatMessage) {
        viewModelScope.launch {
            val newMessage = message.toChatEntity(memberId)

            // 해당 chatId 의 recentChatDate 와 현재 메세지의 sentDate 가 다르면, Date type 메세지 로컬에 저장
            unReadChatData.forEach { data ->
                if (data.chatId == newMessage.chatId) {
                    if (newMessage.sentDate != data.recentChatDate) {
                        storeDateMessage(newMessage.chatId, newMessage.sentDate)
                        return@forEach
                    }
                }
            }

            if (newMessage.type != NOTHING) {
                chatRepository.addChat(newMessage)
            }
        }
    }

    private fun storeDateMessage(chatId: Int, date: String) {
        viewModelScope.launch {
            chatRepository.addChat(
                ChatEntity(
                    chatId = chatId,
                    type = DATE,
                    message = date,
                )
            )
        }
    }

    private fun storeUnReadChat() {
        viewModelScope.launch {
            unReadChatData.forEach {
                when (val response = chatRepository.addUnReadChatData(it.toUnReadChatEntity())) {
                    is BaseState.Success -> {}
                    is BaseState.Error -> {
                        Log.d(TAG, response.msg)
                    }
                }
            }
        }
    }

    fun readChat(chatId: Int) {
        unReadChatData = unReadChatData.map {
            if (it.chatId == chatId) {
                _unReadCnt.value = unReadCnt.value - it.unReadCount
                it.copy(
                    unReadCount = 0
                )
            } else {
                it
            }
        }
    }

    fun exitChat(chatId: Int) {
        viewModelScope.launch {
            chatRepository.deleteChat(chatId)
            chatRepository.deleteUnReadChatData(chatId)
            unReadChatData = unReadChatData.filter {
                it.chatId != chatId
            }
        }
    }

}