package com.aoztg.greengrim.presentation.ui.global.nftdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiNftDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiNftDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NftDetailUiState(
    val nftDetail: UiNftDetail = UiNftDetail()
)

sealed class NftDetailEvents {
    data class ShowSnackMessage(val msg: String) : NftDetailEvents()
}

@HiltViewModel
class NftDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NftDetailUiState())
    val uiState: StateFlow<NftDetailUiState> = _uiState.asStateFlow()

    val btnState = MutableStateFlow("")

    private var nftId = -1

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

                    }
                }
            }
        }
    }

    fun setNftId(id: Int) {
        nftId = id
        getNftDetail()
    }
}