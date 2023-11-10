package com.aoztg.greengrim.presentation.ui.chat.createcertification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CreateCertificationUiState(
    val nextBtnState: BaseState = BaseState.Empty
)

sealed class CreateCertificationEvents{
    object NavigateToBack : CreateCertificationEvents()
}

@HiltViewModel
class CreateCertificationViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(CreateCertificationUiState())
    val uiState: StateFlow<CreateCertificationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateCertificationEvents>()
    val events: SharedFlow<CreateCertificationEvents> = _events.asSharedFlow()

    val description = MutableStateFlow("")
    private val certificationImgUrl = MutableStateFlow("")

    val isDataReady = combine(description, certificationImgUrl){ description, imgUrl ->
        description.isNotBlank() && imgUrl.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImgUrl(url: String){
        certificationImgUrl.value = url
    }

    fun navigateBack(){
        viewModelScope.launch {
            _events.emit(CreateCertificationEvents.NavigateToBack)
        }
    }

}