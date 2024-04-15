package com.aoztg.greengrim.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiMyInfo
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MyPageUiState(
    val uiMyInfo: UiMyInfo = UiMyInfo()
)

sealed class MyPageEvent {
    object NavigateToAttendCheck : MyPageEvent()
    object NavigateToMyWallet : MyPageEvent()
    object NavigateToMyProfile : MyPageEvent()
    object NavigateToMyPoint : MyPageEvent()
    object NavigateToMySetting : MyPageEvent()
    data class NavigateToWebView(val url: String) : MyPageEvent()
    data class ShowToastMessage(val msg: String) : MyPageEvent()
    data class ShowSnackMessage(val msg: String) : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyPageEvent>()
    val events: SharedFlow<MyPageEvent> = _events.asSharedFlow()

    fun getMyInfo() {
        viewModelScope.launch {
            infoRepository.getMyInfo().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiMyInfo = it.body.toUiMyInfo()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(MyPageEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToMySetting(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMySetting)
        }
    }

    fun navigateToAttendCheck() {
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToAttendCheck)
        }
    }
    fun navigateToMyWallet(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMyWallet)
        }
    }

    fun navigateToMyProfile(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMyProfile)
        }
    }

    fun navigateToMyPoint(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMyPoint)
        }
    }

    fun navigateToAnnounce(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToWebView(""))
        }
    }

    fun navigateToPrivacyPolicy(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToWebView(""))
        }
    }
    fun navigateToTerms(){
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToWebView(""))
        }
    }

}
