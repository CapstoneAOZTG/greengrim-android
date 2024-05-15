package com.aoztg.greengrim.presentation.chatmanager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.ChatListDataRequest
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.chatmanager.mapper.toUiChatListItem
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.chatmanager.model.UiChatListItem
import com.aoztg.greengrim.presentation.ui.getCurrentTimeString
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ChatEvent {
    data class ShowToastMessage(val msg: String) : ChatEvent()
    data class ShowSnackMessage(val msg: String) : ChatEvent()
}

@HiltViewModel
class ChatManager @Inject constructor(
    private val chatRepository: ChatRepository,
    private val memberRepository: MemberRepository,
    private val keyDataStoreManager: KeyDataStoreManager
) : ViewModel() {

    private val _events: MutableSharedFlow<ChatEvent> = MutableSharedFlow()
    val event: SharedFlow<ChatEvent> = _events

    private val _newChat = MutableSharedFlow<ChatMessage>()
    val newChat: SharedFlow<ChatMessage> = _newChat.asSharedFlow()

    private val _firstConnect = MutableStateFlow(false)
    val firstConnect: StateFlow<Boolean> = _firstConnect.asStateFlow()

    private val _chatListData = MutableStateFlow<List<UiChatListItem>>(emptyList())
    val chatListData: StateFlow<List<UiChatListItem>> = _chatListData.asStateFlow()

    val unReadCnt = MutableStateFlow(0)

    var isInChatting = false
    var curChatId = 0L

    private var memberId: Long = 0
    private val chatSocket =
        ChatSocket(::receiveMessage, ::showSocketToastMessage, ::showSocketSnackMessage, keyDataStoreManager)

    init {
        setMemberId()
    }

    private fun setMemberId() {
        viewModelScope.launch {
            keyDataStoreManager.getMemberId()?.let {
                memberId = it
            }
        }
    }

    fun getMyChatIds() {
        viewModelScope.launch {
            chatRepository.getUnReadChatData().let {
                when (it) {
                    is BaseState.Success -> {
                        getMyChatListData(it.body)
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatEvent.ShowSnackMessage("데이터 불러오기 실패"))
                    }
                }

            }
        }
    }

    private fun getMyChatListData(list: List<UnReadChatEntity>) {
        // 채팅리스트 데이터 API 연결
        viewModelScope.launch {

            val body = list.map {
                ChatListDataRequest(
                    it.chatId, it.recentReadTime
                )
            }

            chatRepository.getChatListData(body).let {
                when (it) {
                    is BaseState.Success -> {

                        _chatListData.value = it.body.map { data ->
                            data.toUiChatListItem()
                        }

                        if (!firstConnect.value) {
                            chatSocket.connectServer()
                            chatListData.value.forEach { data ->
                                chatSocket.subscribeChat(data.chatId)
                            }
                        }
                        _firstConnect.value = true

                        var count = 0

                        it.body.forEach { data ->
                            count += data.chatroomInfo.newMessageCount
                        }
                        unReadCnt.value = count
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatEvent.ShowSnackMessage("데이터 불러오기 실패"))
                    }
                }
            }
        }
    }

    fun subscribeNewChat(chatId: Long, challengeId: Long, title: String, titleImg: String) {
        chatSocket.subscribeChat(chatId)
        _chatListData.value =
            chatListData.value + UiChatListItem(
                chatId = chatId,
                challengeId = challengeId,
                title = title,
                titleImg = titleImg
            )
        storeRecentReadTime(chatId)
    }

    fun sendMessage(chatId: Long, message: String) {
        chatSocket.sendMessage(
            memberId,
            chatId,
            message
        )
    }

    fun sendCertificationMessage(chatId: Long, message: String, certId: Long, certImg: String) {
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
        if (isInChatting && chatMessage.roomId == curChatId) {
            updateRecentChatData(chatMessage, true)
        } else {
            updateRecentChatData(chatMessage, false)
        }

        // 최신 채팅 위로 올리는 로직
        val newList = chatListData.value.toMutableList()
        var temp = UiChatListItem()
        newList.forEach {
            if (it.chatId == chatMessage.roomId) {
                temp = it
            }
        }
        newList.remove(temp)
        newList.add(0, temp)

        _chatListData.value = newList

        viewModelScope.launch {
            _newChat.emit(chatMessage)
        }
    }

    private fun updateRecentChatData(chatMessage: ChatMessage, onlyRecentMessage: Boolean) {
        _chatListData.update {
            chatListData.value.map { data ->
                if (data.chatId == chatMessage.roomId) {
                    data.copy(
                        recentChat = chatMessage.message,
                        recentTime = chatMessage.sentTime,
                        chatCount = if (onlyRecentMessage) data.chatCount else data.chatCount + 1
                    )
                } else {
                    data
                }
            }
        }

        if (!onlyRecentMessage) {
            unReadCnt.value = unReadCnt.value + 1
        }
    }

    fun inChat(id: Long) {
        isInChatting = true
        curChatId = id

        _chatListData.update {
            it.map { data ->
                if (data.chatId == id) {
                    unReadCnt.value = unReadCnt.value - data.chatCount
                    data.copy(
                        chatCount = 0
                    )
                } else {
                    data
                }
            }
        }
    }

    fun outChat() {
        isInChatting = false
    }

    fun storeRecentReadTime(chatRoomId: Long) {
        viewModelScope.launch {
            chatRepository.addUnReadChatData(
                UnReadChatEntity(
                    chatId = chatRoomId,
                    recentReadTime = getCurrentTimeString()
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {
                        Log.d(TAG, "fail")
                    }
                }
            }
        }
    }

    fun exitChat(chatId: Long) {
        viewModelScope.launch {
            chatRepository.deleteUnReadChatData(chatId)
            _chatListData.update { it.filter { data ->
                data.chatId != chatId
            }}
        }
    }

    private fun showSocketToastMessage(msg: String) {
        viewModelScope.launch {
            _events.emit(ChatEvent.ShowToastMessage(msg))
        }
    }

    private fun showSocketSnackMessage(msg: String) {
        viewModelScope.launch {
            _events.emit(ChatEvent.ShowSnackMessage(msg))
        }
    }

    fun disconnectChat() {
        chatSocket.disconnectServer()
        _firstConnect.value = false
    }
}