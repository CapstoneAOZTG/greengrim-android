package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ChatResponse
import com.aoztg.greengrim.presentation.ui.chat.model.ChatMessage
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
    val chatMessages: List<ChatMessage> = emptyList()
)

sealed class ChatRoomEvents {
    object NavigateBack : ChatRoomEvents()
    object ShowPopupMenu : ChatRoomEvents()
    object DismissPopupMenu : ChatRoomEvents()
    data class NavigateToCreateCertification(val id: Int) : ChatRoomEvents()
    data class NavigateToCertificationList(val id: Int) : ChatRoomEvents()
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

    init {
        observeChatMessage()
    }

    fun newChatMessage(
        nick: String,
        profileImg: String,
        message: String
    ){
        Log.d(TAG,message)
        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + ChatMessage(
                    nick = nick,
                    profileImg = profileImg,
                    message = message,
                    type = OTHER_CHAT
                )
            )
        }
    }

    private fun newMyChatMessage(
        message: String
    ){
        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + ChatMessage(
                    message = message,
                    type = MY_CHAT
                )
            )
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

    fun navigateBack() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateBack)
        }
    }

    fun navigateToCreateCertification() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCreateCertification(challengeId))
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

    fun onRootClicked(){
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.DismissPopupMenu)
        }
    }

    fun sendMessage(){
        viewModelScope.launch{
            _events.emit(ChatRoomEvents.SendMessage(chatRoomId,chatMessage.value))
            newMyChatMessage(chatMessage.value)
            chatMessage.emit("")
            _events.emit(ChatRoomEvents.ScrollBottom)
        }
    }
}