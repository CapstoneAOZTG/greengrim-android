package com.aoztg.greengrim.presentation.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


sealed class IntroEvent {
    object ShowPhotoBottomSheet : IntroEvent()
    object GoToMainActivity : IntroEvent()
}

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val imageRepository: ImageRepository
) :
    ViewModel() {

    private val _events = MutableSharedFlow<IntroEvent>()
    val events: MutableSharedFlow<IntroEvent> = _events

    private val _profileImg = MutableStateFlow("")
    val profileImg: StateFlow<String> = _profileImg.asStateFlow()

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

    fun imageToUrl(file: MultipartBody.Part) {
        viewModelScope.launch {
            val response = imageRepository.imageToUrl(file)

            if (response.isSuccessful) {

                response.body()?.let {
                    _profileImg.value = it.imgUrl
                }
            }
        }
    }
}