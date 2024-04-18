package com.aoztg.greengrim.presentation.ui.nft.exchange.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.EditNftRequest
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditExchangedNftEvent {
    object NavigateToBack : EditExchangedNftEvent()
    data class NavigateToNftDetail(val id: Long) : EditExchangedNftEvent()
    data class ShowCustomSnack(val msg: String) : EditExchangedNftEvent()
    data class ShowToastMessage(val msg: String) : EditExchangedNftEvent()
}

@HiltViewModel
class EditExchangedNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<EditExchangedNftEvent>()
    val events: SharedFlow<EditExchangedNftEvent> = _events.asSharedFlow()

    private var nftId = -1L
    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val imgUrl = MutableStateFlow("")

    val isDataReady = combine(title, description, imgUrl) { title, description, imgUrl ->
        title.isNotBlank() && description.isNotBlank() && imgUrl.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setInfo(img: String, id: Long) {
        imgUrl.value = img
        nftId = id
    }

    fun editNft() {
        viewModelScope.launch {
            nftRepository.editExchangedNft(
                EditNftRequest(
                    nftId, title.value, description.value
                )
            ).let {
                when (it) {
                    is BaseState.Success -> _events.emit(
                        EditExchangedNftEvent.NavigateToNftDetail(
                            it.body.nftInfo.id
                        )
                    )

                    is BaseState.Error -> _events.emit(EditExchangedNftEvent.ShowCustomSnack(it.msg))
                }
            }

        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(EditExchangedNftEvent.NavigateToBack)
        }
    }
}