package com.aoztg.greengrim.presentation.ui.intro

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.data.repository.IntroRepository
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


sealed class IntroEvent {
    object ShowPhotoBottomSheet : IntroEvent()
    object GoToMainActivity : IntroEvent()
    data class ShowToastMessage(val msg: String) : IntroEvent()
    data class ShowSnackMessage(val msg: String) : IntroEvent()
}

@HiltViewModel
class IntroViewModel @Inject constructor() :
    ViewModel() {

    private val _events = MutableSharedFlow<IntroEvent>()
    val events: MutableSharedFlow<IntroEvent> = _events

    private val _imageUri = MutableSharedFlow<Uri>()
    val imageUri: SharedFlow<Uri> = _imageUri.asSharedFlow()

    private val _imageFile = MutableSharedFlow<MultipartBody.Part>()
    val imageFile: SharedFlow<MultipartBody.Part> = _imageFile.asSharedFlow()

    fun goToGallery() {
        viewModelScope.launch {
            _events.emit(
                IntroEvent.ShowPhotoBottomSheet
            )
        }
    }

    fun goToMain() {
        viewModelScope.launch {
            _events.emit(
                IntroEvent.GoToMainActivity
            )
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
}