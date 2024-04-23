package com.aoztg.greengrim.presentation.chatmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.local.UnReadChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.ui.getCurrentTimeString
import com.aoztg.greengrim.presentation.util.Constants
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
    data class ShowSnackMessage(val msg: String) : ChatEvent()
}

@HiltViewModel
class ChatManager @Inject constructor(
    private val chatRepository: ChatRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _events: MutableSharedFlow<ChatEvent> = MutableSharedFlow()
    val event: SharedFlow<ChatEvent> = _events

    private val _newChat = MutableSharedFlow<ChatMessage>()
    val newChat: SharedFlow<ChatMessage> = _newChat.asSharedFlow()

    private val _firstConnect = MutableStateFlow(false)
    val firstConnect: StateFlow<Boolean> = _firstConnect.asStateFlow()

    private val _initialConnectChatIds = MutableStateFlow<List<Long>>(emptyList())
    val initialConnectChatIds: StateFlow<List<Long>> = _initialConnectChatIds.asStateFlow()

    val unReadCnt = MutableStateFlow(0)

    private var memberId: Long = 0
    private val chatSocket =
        ChatSocket(::receiveMessage, ::showSocketToastMessage, ::showSocketSnackMessage)

    init {
        setMemberId()
    }

    private fun setMemberId() {
        val memberId: Long = App.sharedPreferences.getLong(Constants.MEMBER_ID, -1L)
        if (memberId != -1L) {
            this.memberId = memberId
        } else {

        }
    }

    fun getMyChatIds() {
        viewModelScope.launch {
            chatRepository.getUnReadChatData().let {
                when (it) {
                    is BaseState.Success -> {
                        _initialConnectChatIds.value = it.body.map { data ->
                            data.chatId
                        }

                        chatSocket.connectServer()
                        initialConnectChatIds.value.forEach { chatId ->
                            chatSocket.subscribeChat(chatId)
                        }
                        getMyChatListData()
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatEvent.ShowSnackMessage(it.msg))
                    }
                }
                _firstConnect.value = true
            }
        }
    }

    fun getMyChatListData(){
        // 채팅리스트 데이터 API 연결
    }

    fun subscribeNewChat(chatId: Long) {
        chatSocket.subscribeChat(chatId)
        storeRecentReadTime(chatId)
        _initialConnectChatIds.value = initialConnectChatIds.value + chatId
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
        viewModelScope.launch {
            _newChat.emit(chatMessage)
        }
    }

    fun storeRecentReadTime(chatRoomId : Long){
        viewModelScope.launch {
            chatRepository.addUnReadChatData(
                UnReadChatEntity(
                    chatId = chatRoomId,
                    recentReadTime = getCurrentTimeString()
                )
            ).let{
                when(it){
                    is BaseState.Success -> {

                    }
                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    fun exitChat(chatId: Long) {
        viewModelScope.launch {
            chatRepository.deleteUnReadChatData(chatId)
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
    }
}