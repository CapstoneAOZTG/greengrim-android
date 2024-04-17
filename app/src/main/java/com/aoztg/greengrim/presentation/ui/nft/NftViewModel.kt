package com.aoztg.greengrim.presentation.ui.nft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.nft.model.UiGrimItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NftUiState(
    val hotNftList: List<UiNftItem> = emptyList(),
    val grimList: List<UiGrimItem> = emptyList(),
    val sortType: NftSortType = NftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class NftEvent {
    data class NavigateToNftDetail(val id: Long) : NftEvent()
    object ShowBottomSheet : NftEvent()
    data class ShowSnackMessage(val msg: String) : NftEvent()
    object ShowLoading : NftEvent()
    object DismissLoading : NftEvent()
    object NavigateToNftList : NftEvent()
}

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(NftUiState())
    val uiState: StateFlow<NftUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftEvent>()
    val events: SharedFlow<NftEvent> = _events.asSharedFlow()

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(NftEvent.NavigateToNftDetail(id))
        }
    }

    fun getNftList(){
        if(uiState.value.hasNext){
            viewModelScope.launch {
                nftRepository.getExchangedNftList(
                    uiState.value.page,
                    10,
                    uiState.value.sortType.value
                ).let{
                    when(it){
                        is BaseState.Success -> {

                        }

                        is BaseState.Error -> {

                        }
                    }
                }
            }
        }

    }

    fun navigateToNftList() {
        viewModelScope.launch {
            _events.emit(NftEvent.NavigateToNftList)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(NftEvent.ShowBottomSheet)
        }
    }

    fun setSortType(type: NftSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )
    }

}

