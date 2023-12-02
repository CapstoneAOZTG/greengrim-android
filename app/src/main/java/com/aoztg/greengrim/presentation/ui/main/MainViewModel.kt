package com.aoztg.greengrim.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.FcmRepository
import com.aoztg.greengrim.data.repository.ImageRepository
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
    data class ShowToastMessage(val msg: String) : MainEvent()
    data class ShowSnackMessage(val msg: String) : MainEvent()
}

enum class KeyboardState {
    NONE,
    SHOW,
    HIDE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) :
    ViewModel() {

    private val _events: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val event: SharedFlow<MainEvent> = _events

    private val _image = MutableSharedFlow<String>()
    val image: SharedFlow<String> = _image.asSharedFlow()

    private val _keyboardState = MutableStateFlow(KeyboardState.NONE)
    val keyboardState: StateFlow<KeyboardState> = _keyboardState.asStateFlow()


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
                        _events.emit(MainEvent.ShowSnackMessage(it.msg))
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

    fun showKeyboard() {
        _keyboardState.value = KeyboardState.SHOW
    }

    fun hideKeyboard() {
        _keyboardState.value = KeyboardState.HIDE
    }
}