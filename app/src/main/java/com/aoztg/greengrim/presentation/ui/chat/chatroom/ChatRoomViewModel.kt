package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val editTextState: Boolean = false
)

sealed class ChatRoomEvents {
    object NavigateBack : ChatRoomEvents()
    object ShowPopupMenu : ChatRoomEvents()
    data class NavigateToMakeCertification(val id: String) : ChatRoomEvents()
    data class NavigateToCertificationList(val id: String) : ChatRoomEvents()
}

@HiltViewModel
class ChatRoomViewModel @Inject constructor() : ViewModel() {

    private var chatRoomId = ""

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatRoomEvents>()
    val events: SharedFlow<ChatRoomEvents> = _events.asSharedFlow()

    val chatMessage = MutableStateFlow("")

    init {
        observeChatMessage()
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

    fun setChatId(id: String) {
        chatRoomId = id
    }
}