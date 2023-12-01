package com.aoztg.greengrim.presentation.ui.chat.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.local.ChatEntity
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChatMessage
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.DATE
import com.aoztg.greengrim.presentation.util.Constants.NOTHING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    val chatMessages: List<UiChatMessage> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class ChatRoomEvents {
    object NavigateBack : ChatRoomEvents()
    object ExitChat: ChatRoomEvents()
    object ShowPopupMenu : ChatRoomEvents()
    object NavigateToCreateCertification : ChatRoomEvents()
    data class NavigateToCertificationList(val id: Int) : ChatRoomEvents()
    data class NavigateToCertificationDetail(val id: Int) : ChatRoomEvents()
    data class SendMessage(val chatId: Int, val message: String) : ChatRoomEvents()
    object ScrollBottom : ChatRoomEvents()
    data class ShowToastMessage(val msg: String) : ChatRoomEvents()
}

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    var chatRoomId = -1
    var challengeId = -1

    companion object {
        const val SCROLL_GET = 0
        const val INIT_GET = 1
    }

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatRoomEvents>()
    val events: SharedFlow<ChatRoomEvents> = _events.asSharedFlow()

    val chatMessage = MutableStateFlow("")

    private var memberId: Long = 0
    private var lastDate: String = ""

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

    fun getChatMessageData() {
        if (uiState.value.hasNext) {
            viewModelScope.launch {
                when (val response = chatRepository.getChat(chatRoomId, uiState.value.page)) {
                    is BaseState.Success -> {
                        val list = response.body.chatEntityList.map {
                            it.toUiChatMessage(::navigateToCertificationDetail)
                        }
                        _uiState.update { state ->
                            state.copy(
                                hasNext = response.body.hasNext,
                                chatMessages = uiState.value.chatMessages + list,
                                page = uiState.value.page + 1
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatRoomEvents.ShowToastMessage(response.msg))
                    }
                }
            }
        }

    }

    fun newChatMessage(
        message: ChatMessage
    ) {
        val newMessages = uiState.value.chatMessages.toMutableList()
        val newMessage = message.toUiChatMessage(memberId,::navigateToCertificationDetail)

        if(newMessages.isNotEmpty()){
            if(newMessages.last().sentDate != newMessage.sentDate){
                newMessages.add(0, UiChatMessage(type = DATE, sentDate = newMessage.sentDate){})
            }
        }

        if(newMessage.type != NOTHING){
            newMessages.add(0, newMessage)
            _uiState.update { state ->
                state.copy(
                    chatMessages = newMessages
                )
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

    private fun scrollBottom() {
        viewModelScope.launch {
            delay(10)
            _events.emit(ChatRoomEvents.ScrollBottom)
        }
    }

    fun setIds(chatIdData: Int, challengeIdData: Int) {
        chatRoomId = chatIdData
        challengeId = challengeIdData
        getChatMessageData()
    }

    fun sendMessage() {
        viewModelScope.launch {
            _events.emit(
                ChatRoomEvents.SendMessage(
                    chatRoomId,
                    chatMessage.value,
                )
            )
            chatMessage.emit("")
        }
    }

    fun exitChallenge(){
        viewModelScope.launch {
            challengeRepository.exitChallenge(challengeId)
            _events.emit(ChatRoomEvents.ExitChat)
        }
    }

}