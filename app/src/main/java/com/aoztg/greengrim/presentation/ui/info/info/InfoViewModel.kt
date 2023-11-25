package com.aoztg.greengrim.presentation.ui.info.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.presentation.ui.info.mapper.toMyProfileInfo
import com.aoztg.greengrim.presentation.ui.info.model.MyProfileInfo
import com.google.gson.Gson
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


data class InfoUiState(
    val myProfileInfo: MyProfileInfo = MyProfileInfo()
)

sealed class InfoEvents {
    object ShowBottomSheet : InfoEvents()
    object GoToIntroActivity : InfoEvents()
    object NavigateToEditProfile: InfoEvents()
    object NavigateToAttendCheck: InfoEvents()
    object NavigateToMyChallenge: InfoEvents()
    object NavigateToMyCertification: InfoEvents()
    data class ShowToastMessage(val msg: String): InfoEvents()
}

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState:StateFlow<InfoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<InfoEvents>()
    val events: SharedFlow<InfoEvents> = _events.asSharedFlow()

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(InfoEvents.ShowBottomSheet)
        }
    }

    fun getProfileInfo(){
        viewModelScope.launch {
            val response = infoRepository.getProfile()

            if(response.isSuccessful){
                response.body()?.let{ body ->
                    _uiState.update { state ->
                        state.copy(
                            myProfileInfo = body.toMyProfileInfo()
                        )
                    }
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(InfoEvents.ShowToastMessage(error.message))
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            val response = infoRepository.withdrawal()

            if (response.isSuccessful) {
                _events.emit(InfoEvents.GoToIntroActivity)
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(InfoEvents.ShowToastMessage(error.message))
            }
        }
    }

    fun navigateToEditProfile(){
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToEditProfile)
        }
    }

    fun navigateToAttendCheck(){
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToAttendCheck)
        }
    }

    fun navigateToMyCertification(){
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToMyCertification)
        }
    }

    fun navigateToMyChallenge(){
        viewModelScope.launch{
            _events.emit(InfoEvents.NavigateToMyChallenge)
        }
    }
}
