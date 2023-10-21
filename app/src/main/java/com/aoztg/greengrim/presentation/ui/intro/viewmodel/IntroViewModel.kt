package com.aoztg.greengrim.presentation.ui.intro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class IntroEvent {
    object GoToGallery : IntroEvent()
    object GoToMainActivity : IntroEvent()
}

@HiltViewModel
class IntroViewModel @Inject constructor(private val introRepository: IntroRepository) :
    ViewModel() {

    private val _events = MutableSharedFlow<IntroEvent>()
    val events: MutableSharedFlow<IntroEvent> = _events

    private val _profileImg = MutableStateFlow("")
    val profileImg: StateFlow<String> = _profileImg.asStateFlow()

    var emailData = ""

    fun goToGallery() {
        viewModelScope.launch {
            _events.emit(
                IntroEvent.GoToGallery
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

    fun setProfile(url: String) {
        viewModelScope.launch {
            _profileImg.value = url
        }
    }
}