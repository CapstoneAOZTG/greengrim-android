package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChatMessage
import com.aoztg.greengrim.presentation.ui.main.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.MY_CHAT
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT
import com.aoztg.greengrim.presentation.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatRoomUiState(
    val editTextState: Boolean = false,
    val chatMessages: List<UiChatMessage> = emptyList()
)

sealed class ChatRoomEvents {
    object NavigateBack : ChatRoomEvents()
    object ShowPopupMenu : ChatRoomEvents()
    object NavigateToCreateCertification : ChatRoomEvents()
    data class NavigateToCertificationList(val id: Int) : ChatRoomEvents()
    data class NavigateToCertificationDetail(val id: Int) : ChatRoomEvents()
    data class SendMessage(val chatId: Int, val message: String) : ChatRoomEvents()
    object ScrollBottom: ChatRoomEvents()
}

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : ViewModel() {

    var chatRoomId = -1
    var challengeId = -1

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatRoomEvents>()
    val events: SharedFlow<ChatRoomEvents> = _events.asSharedFlow()

    val chatMessage = MutableStateFlow("")

    private var memberId: Long = 0

    init {
        setMemberId()
        observeChatMessage()
    }

    private fun setMemberId() {
        val memberId: Long = App.sharedPreferences.getLong(Constants.MEMBER_ID, -1L)
        if (memberId != -1L) {
            this.memberId = memberId
        } else {

        }
    }

    private fun observeChatMessage() {
        chatMessage.onEach {
            Log.d(TAG, it)
            if (it.isNotBlank()) {
                _uiState.update { state ->
                    state.copy(
                        editTextState = true
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        editTextState = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun newChatMessage(
        message: ChatMessage
    ){
        if(message.senderId == memberId){
            // 내 채팅
            if(message.certId == -1){
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + message.toUiChatMessage(MY_CHAT){}
                    )
                }
            } else {
                // 인증 채팅
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + message.toUiChatMessage(MY_CHAT, ::navigateToCertificationDetail)
                    )
                }
            }

        } else {
            // 남 채팅
            if(message.certId == -1){
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + message.toUiChatMessage(OTHER_CHAT){}
                    )
                }
            } else {
                // 인증 채팅
                _uiState.update { state ->
                    state.copy(
                        chatMessages = state.chatMessages + message.toUiChatMessage(OTHER_CHAT, ::navigateToCertificationDetail)
                    )
                }
            }
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateBack)
        }
    }

    fun navigateToCreateCertification() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCreateCertification)
        }
    }

    private fun navigateToCertificationDetail(certId: Int) {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCertificationDetail(certId))
        }
    }

    fun showPopupMenu() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.ShowPopupMenu)
        }
    }

    fun setIds(chatIdData: Int, challengeIdData: Int) {
        chatRoomId = chatIdData
        challengeId = challengeIdData
    }

    fun sendMessage(){
        viewModelScope.launch{
            _events.emit(ChatRoomEvents.SendMessage(
                chatRoomId,
                chatMessage.value,
            ))
            chatMessage.emit("")
            _events.emit(ChatRoomEvents.ScrollBottom)
        }
    }
}