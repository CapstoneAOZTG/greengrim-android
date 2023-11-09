package com.aoztg.greengrim.presentation.ui.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data class NavigateToChatRoom(val id: Int) : ChatListEvents()
}


@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatListEvents>()
    val events: SharedFlow<ChatListEvents> = _events.asSharedFlow()

    fun getChatList() {
        _uiState.update { state ->
            state.copy(
                chatListItem = listOf(
                    ChatListItem(
                        id = 0,
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        recentChat = "인증 하셨나요 다들 ㅋㅋㅋ",
                        recentTime = "오전 5:31",
                        creationDday = "오늘",
                        ::navigateToChatRoom
                    ),
                    ChatListItem(
                        id = 0,
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        recentChat = "인증 하셨나요 다들 ㅋㅋㅋ",
                        recentTime = "오전 5:31",
                        creationDday = "오늘",
                        ::navigateToChatRoom
                    ),
                    ChatListItem(
                        id = 0,
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        recentChat = "인증 하셨나요 다들 ㅋㅋㅋ",
                        recentTime = "오전 5:31",
                        creationDday = "3일 전",
                        ::navigateToChatRoom
                    ),
                    ChatListItem(
                        id = 0,
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        recentChat = "인증 하셨나요 다들 ㅋㅋㅋ",
                        recentTime = "오전 5:31",
                        creationDday = "1달 전",
                        ::navigateToChatRoom
                    )

                )
            )
        }
    }

    private fun navigateToChatRoom(id: Int) {
        viewModelScope.launch {
            _events.emit(ChatListEvents.NavigateToChatRoom(id))
        }
    }
}