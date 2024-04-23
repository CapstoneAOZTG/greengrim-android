package com.aoztg.greengrim.presentation.ui.chat.chatlist

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


sealed class ChatListEvents {
    data class ShowToastMessage(val msg: String) : ChatListEvents()
    object CallUnReadChatData : ChatListEvents()
    data class ShowSnackMessage(val msg: String) : ChatListEvents()
    object ShowLoading : ChatListEvents()
    object DismissLoading : ChatListEvents()
}


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<ChatListEvents>()
    val events: SharedFlow<ChatListEvents> = _events.asSharedFlow()

}