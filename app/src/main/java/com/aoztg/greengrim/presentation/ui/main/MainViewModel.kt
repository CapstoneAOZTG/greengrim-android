package com.aoztg.greengrim.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

sealed class MainEvent {
    object HideBottomNav : MainEvent()
    object ShowBottomNav : MainEvent()
    object ShowPhotoBottomSheet : MainEvent()
    object Logout: MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {

    private val _events: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val event: SharedFlow<MainEvent> = _events

    private val _image = MutableStateFlow("")
    val image: StateFlow<String> = _image.asStateFlow()

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
            val response = imageRepository.imageToUrl(file)

            if (response.isSuccessful) {

                response.body()?.let {
                    _image.value = it.imgUrl
                }
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            _events.emit(MainEvent.Logout)
        }
    }

}