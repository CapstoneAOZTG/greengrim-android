package com.aoztg.greengrim.presentation.ui.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.ui.chat.mapper.toChatListItem
import com.aoztg.greengrim.presentation.ui.chat.model.ChatListItem
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


data class ChatListUiState(
    val chatListItem: List<ChatListItem> = emptyList()
)

sealed class ChatListEvents {
    data class NavigateToChatRoom(val chatId: Int, val challengeId: Int) : ChatListEvents()
}


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatListEvents>()
    val events: SharedFlow<ChatListEvents> = _events.asSharedFlow()

    fun getChatList() {
        viewModelScope.launch {
            val response = chatRepository.getChatRooms()

            if( response.isSuccessful ){
                response.body()?.let{ body ->

                    _uiState.update { state ->
                        state.copy(
                            chatListItem = body.map{
                                it.toChatListItem(::navigateToChatRoom)
                            }
                        )
                    }
                }
            } else {

            }
        }
    }

    private fun navigateToChatRoom(chatId: Int, challengeId: Int) {
        viewModelScope.launch {
            _events.emit(ChatListEvents.NavigateToChatRoom(chatId, challengeId))
        }
    }
}