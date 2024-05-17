package com.aoztg.greengrim.presentation.ui.chat.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.chatmanager.model.ChatMessage
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChatInfo
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChatMessage
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChatMessageItem
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatInfo
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
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
    val hasNext: Boolean = true,
    val chatInfo: UiChatInfo = UiChatInfo()
)

sealed class ChatRoomEvents {
    object NavigateBack : ChatRoomEvents()
    object ExitChat : ChatRoomEvents()
    object ShowPopupMenu : ChatRoomEvents()
    object ShowTodayCertification : ChatRoomEvents()
    object NavigateToCreateCertification : ChatRoomEvents()
    data class NavigateToCertificationList(val id: Long) : ChatRoomEvents()
    data class NavigateToCertificationDetail(val id: Long) : ChatRoomEvents()
    data class NavigateToProfile(val id: Long) : ChatRoomEvents()
    data class SendMessage(val chatId: Long, val message: String) : ChatRoomEvents()
    object ScrollBottom : ChatRoomEvents()
    data class ShowToastMessage(val msg: String) : ChatRoomEvents()
    data class ShowSnackMessage(val msg: String) : ChatRoomEvents()
    object ShowLoading : ChatRoomEvents()
    object DismissLoading : ChatRoomEvents()
}

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val challengeRepository: ChallengeRepository,
    private val keyDataStoreManager: KeyDataStoreManager
) : ViewModel() {

    var chatRoomId = -1L
    var challengeId = -1L

    companion object {
        const val SCROLL_GET = 0
        const val INIT_GET = 1
    }

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatRoomEvents>()
    val events: SharedFlow<ChatRoomEvents> = _events.asSharedFlow()

    val chatMessage = MutableStateFlow("")

    private var isDialogShown = true
    private var memberId: Long = 0
    private var lastDate: String = ""

    init {
        setMemberId()
        observeChatMessage()
    }

    private fun setMemberId() {
        viewModelScope.launch {
            keyDataStoreManager.getMemberId()?.let {
                memberId = it
            } ?: run {

            }
        }

    }

    fun getChatInfo() {
        viewModelScope.launch {
            chatRepository.getChatInfo(chatRoomId).let {
                when (it) {
                    is BaseState.Success -> {
                        val newData = it.body.toUiChatInfo()
                        if (uiState.value.chatInfo != newData) {
                            _uiState.update { state ->
                                state.copy(
                                    chatInfo = newData
                                )
                            }
                        }

                        val state = ChatRoomDialogState.stateMap[chatRoomId] ?: true
                        if (!uiState.value.chatInfo.todayCertification && state) {
                            _events.emit(ChatRoomEvents.ShowTodayCertification)
                            ChatRoomDialogState.stateMap[chatRoomId] = false
                        }

                    }

                    is BaseState.Error -> _events.emit(ChatRoomEvents.ShowSnackMessage(it.msg))
                }
            }
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
                when (val response =
                    chatRepository.getChatMessage(chatRoomId, uiState.value.page, 20)) {
                    is BaseState.Success -> {

                        _uiState.update { state ->
                            state.copy(
                                hasNext = response.body.hasNext,
                                chatMessages = uiState.value.chatMessages + response.body.result.map {
                                    it.toUiChatMessageItem(
                                        memberId,
                                        ::navigateToCertificationDetail,
                                        ::navigateToProfile
                                    )
                                },
                                page = uiState.value.page + 1
                            )
                        }

                    }

                    is BaseState.Error -> {
                        _events.emit(ChatRoomEvents.ShowSnackMessage(response.msg))
                    }
                }
            }
        }
    }

    fun newChatMessage(
        message: ChatMessage
    ) {
        val newMessages = uiState.value.chatMessages.toMutableList()
        val newMessage =
            message.toUiChatMessage(memberId, ::navigateToCertificationDetail, ::navigateToProfile)

        if (newMessages.size > 0 && newMessages.first().sentDate.isNotBlank()) {

            if (newMessages.first().sentDate != newMessage.sentDate) {
                newMessages.add(0, UiChatMessage(type = DATE, message = newMessage.sentDate))
            }
        }

        if (newMessage.type != NOTHING) {
            newMessages.add(0, newMessage)
            _uiState.update { state ->
                state.copy(
                    chatMessages = newMessages
                )
            }
            scrollBottom()
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

    private fun navigateToCertificationDetail(certId: Long) {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCertificationDetail(certId))
        }
    }

    private fun navigateToProfile(id: Long) {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToProfile(id))
        }
    }

    fun showPopupMenu() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.ShowPopupMenu)
        }
    }

    private fun scrollBottom() {
        viewModelScope.launch {
            delay(50)
            _events.emit(ChatRoomEvents.ScrollBottom)
        }
    }

    fun setIds(chatIdData: Long, challengeIdData: Long) {
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

    fun exitChallenge() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.ShowLoading)

            challengeRepository.exitChallenge(challengeId).let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(ChatRoomEvents.ExitChat)
                    }

                    is BaseState.Error -> {
                        _events.emit(ChatRoomEvents.DismissLoading)
                        _events.emit(ChatRoomEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToCertificationList() {
        viewModelScope.launch {
            _events.emit(ChatRoomEvents.NavigateToCertificationList(chatRoomId))
        }
    }

}