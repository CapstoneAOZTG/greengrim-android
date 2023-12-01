package com.aoztg.greengrim.presentation.ui.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.ui.chat.mapper.toChatListItem
import com.aoztg.greengrim.presentation.ui.chat.model.ChatListItem
import com.aoztg.greengrim.presentation.ui.main.model.UiUnReadChatData
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
    data class ShowToastMessage(val msg: String) : ChatListEvents()
    object CallUnReadChatData: ChatListEvents()
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
            chatRepository.getChatRooms().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                chatListItem = it.body.map { data ->
                                    data.toChatListItem(::navigateToChatRoom)
                                }
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatListEvents.ShowToastMessage(it.msg))
                    }
                }

                _events.emit(ChatListEvents.CallUnReadChatData)
            }

        }
    }

    fun setRecentChatData(list: List<UiUnReadChatData>){

        _uiState.update { state ->
            state.copy(
                chatListItem = uiState.value.chatListItem.mapIndexed { i, item -> 
                    item.copy(
                        recentChat = list[i].recentChat,
                        recentTime = list[i].recentChatTime,
                        chatCount = list[i].unReadCount
                    )
                }
            )
        }
    }

    private fun navigateToChatRoom(chatId: Int, challengeId: Int) {
        viewModelScope.launch {
            _events.emit(ChatListEvents.NavigateToChatRoom(chatId, challengeId))
        }
    }
}