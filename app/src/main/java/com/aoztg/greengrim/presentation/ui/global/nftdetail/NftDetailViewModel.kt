package com.aoztg.greengrim.presentation.ui.global.nftdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.NftLikeRequest
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftDetail
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftDetailInfo
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

data class NftDetailUiState(
    val nftDetail: UiNftDetailInfo = UiNftDetailInfo()
)

sealed class NftDetailEvents {
    data class ShowSnackMessage(val msg: String) : NftDetailEvents()
    object NavigateToBack : NftDetailEvents()
    data class ShowToastMessage(val msg: String) : NftDetailEvents()
}

@HiltViewModel
class NftDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NftDetailUiState())
    val uiState: StateFlow<NftDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftDetailEvents>()
    val events: SharedFlow<NftDetailEvents> = _events.asSharedFlow()

    private var nftId = -1L


    fun setNftId(id: Long) {
        nftId = id
        getNftDetail()
    }

    private fun getNftDetail() {
        viewModelScope.launch {
            nftRepository.getNftDetail(nftId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nftDetail = it.body.toUiNftDetail()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(NftDetailEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun nftLike() {
        viewModelScope.launch {
            nftRepository.nftLike(
                NftLikeRequest(
                    nftId
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {

                        if (uiState.value.nftDetail.liked) {
                            _uiState.update { state ->
                                state.copy(
                                    nftDetail = uiState.value.nftDetail.copy(
                                        liked = false
                                    )
                                )
                            }

                            _events.emit(NftDetailEvents.ShowToastMessage("좋아요를 취소했어요!"))
                        } else {
                            _uiState.update { state ->
                                state.copy(
                                    nftDetail = uiState.value.nftDetail.copy(
                                        liked = true
                                    )
                                )
                            }

                            _events.emit(NftDetailEvents.ShowToastMessage("좋아요를 눌렀어요!"))
                        }

                    }

                    is BaseState.Error -> _events.emit(NftDetailEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(NftDetailEvents.NavigateToBack)
        }
    }
}