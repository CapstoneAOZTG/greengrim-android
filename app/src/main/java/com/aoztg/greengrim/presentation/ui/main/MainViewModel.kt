package com.aoztg.greengrim.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.presentation.util.Constants
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class MainEvent {
    object HideBottomNav : MainEvent()
    object ShowBottomNav : MainEvent()
    object ShowPhotoBottomSheet : MainEvent()
    object Logout : MainEvent()
    data class SubscribeMyChats(val myChatIds: List<Int>) : MainEvent()
    data class SubscribeNewChat(val chatId: Int) : MainEvent()
    data class ShowToastMessage(val msg: String) : MainEvent()
    data class SendChat(val memberId: Long, val chatId: Int, val message: String) : MainEvent()
    data class SendCertification(
        val memberId: Long,
        val chatId: Int,
        val message: String,
        val certId: Int,
        val certImg: String
    ) : MainEvent()
}

enum class KeyboardState {
    NONE,
    SHOW,
    HIDE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val chatRepository: ChatRepository
) :
    ViewModel() {

    private val _events: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val event: SharedFlow<MainEvent> = _events

    private val _image = MutableSharedFlow<String>()
    val image: SharedFlow<String> = _image.asSharedFlow()

    private val _newChat = MutableSharedFlow<ChatMessage>()
    val newChat: SharedFlow<ChatMessage> = _newChat.asSharedFlow()

    private val _firstConnect = MutableStateFlow(false)
    val firstConnect: StateFlow<Boolean> = _firstConnect.asStateFlow()

    private val _keyboardState = MutableStateFlow(KeyboardState.NONE)
    val keyboardState: StateFlow<KeyboardState> = _keyboardState.asStateFlow()

    private var memberId: Long = 0

    init {
        setMemberId()
        getMyChatIds()
    }

    private fun setMemberId() {
        val memberId: Long = App.sharedPreferences.getLong(Constants.MEMBER_ID, -1L)
        if (memberId != -1L) {
            this.memberId = memberId
        } else {

        }
    }

    private fun getMyChatIds() {
        viewModelScope.launch {
            chatRepository.getChatRooms().let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(MainEvent.SubscribeMyChats(
                            it.body.map { data ->
                                data.chatroomId
                            }
                        ))
                    }

                    is BaseState.Error -> {
                        _events.emit(MainEvent.ShowToastMessage(it.msg))
                    }
                }
                _firstConnect.value = true
            }
        }
    }

    fun goToGallery() {
        viewModelScope.launch {
            _events.emit(
                MainEvent.ShowPhotoBottomSheet
            )
        }
    }

    fun showBNV() {
        viewModelScope.launch {
            _events.emit(MainEvent.ShowBottomNav)
        }
    }

    fun hideBNV() {
        viewModelScope.launch {
            _events.emit(MainEvent.HideBottomNav)
        }
    }

    fun imageToUrl(file: MultipartBody.Part) {
        viewModelScope.launch {
            imageRepository.imageToUrl(file).let {
                when (it) {
                    is BaseState.Success -> {
                        _image.emit(it.body.imgUrl)
                    }

                    is BaseState.Error -> {
                        _events.emit(MainEvent.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _events.emit(MainEvent.Logout)
        }
    }

    fun subscribeNewChat(chatId: Int) {
        viewModelScope.launch {
            _events.emit(MainEvent.SubscribeNewChat(chatId))
        }
    }

    fun sendMessage(chatId: Int, message: String) {
        viewModelScope.launch {
            _events.emit(MainEvent.SendChat(memberId, chatId, message))
        }
    }

    fun sendCertificationMessage(chatId: Int, message: String, certId: Int, certImg: String) {
        viewModelScope.launch {
            _events.emit(MainEvent.SendCertification(memberId, chatId, message, certId, certImg))
        }
    }

    fun receiveMessage(payload: String) {
        val chatMessage = Gson().fromJson(payload, ChatMessage::class.java)
        viewModelScope.launch {
            _newChat.emit(chatMessage)
        }
        storeChatMessage(chatMessage)
    }

    private fun storeChatMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatRepository.addChat(message.toChatEntity(memberId))
        }
    }

    fun showKeyboard() {
        _keyboardState.value = KeyboardState.SHOW
    }

    fun hideKeyboard() {
        _keyboardState.value = KeyboardState.HIDE
    }
}