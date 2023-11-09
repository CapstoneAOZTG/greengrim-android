package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.chat.model.ChatMessage
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
    data class NavigateToMakeCertification(val id: Int) : ChatRoomEvents()
    data class NavigateToCertificationList(val id: Int) : ChatRoomEvents()
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

    fun newChatMessage(){
        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + ChatMessage(
                    nick = "킹보라",
                    profileImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/e0aab34a-c95d-4128-b8b5-1fea618b3236.jpg",
                    message = "아하하하하하하하하하ㅏㅎ",
                    type = 1
                )
            )
        }
    }

    fun newMyChatMessage(){
        _uiState.update { state ->
            state.copy(
                chatMessages = state.chatMessages + ChatMessage(
                    message = "히히히히히히히히",
                    type = 0
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

    fun navigateToMakeCertification() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToMakeCertification(chatRoomId))
        }
    }

    fun navigateToCertificationList() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCertificationList(chatRoomId))
        }
    }

    fun showPopupMenu() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.ShowPopupMenu)
        }
    }

    fun setChatId(id: Int) {
        chatRoomId = id
    }

    fun onRootClicked(){
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.DismissPopupMenu)
        }
    }
}