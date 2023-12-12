package com.aoztg.greengrim.presentation.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data class CopyInClipBoard(val link: String) : MainEvent()
}

enum class KeyboardState {
    NONE,
    SHOW,
    HIDE
}

@HiltViewModel
class MainViewModel @Inject constructor() :
    ViewModel() {

    private val _events: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val event: SharedFlow<MainEvent> = _events

    private val _imageUri = MutableSharedFlow<Uri>()
    val imageUri: SharedFlow<Uri> = _imageUri.asSharedFlow()

    private val _imageFile = MutableSharedFlow<MultipartBody.Part>()
    val imageFile: SharedFlow<MultipartBody.Part> = _imageFile.asSharedFlow()

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

    fun setImage(
        uri: Uri,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _imageUri.emit(uri)
            _imageFile.emit(file)
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

    fun copyInClipBoard(link: String) {
        viewModelScope.launch {
            _events.emit(MainEvent.CopyInClipBoard(link))
        }
    }

}