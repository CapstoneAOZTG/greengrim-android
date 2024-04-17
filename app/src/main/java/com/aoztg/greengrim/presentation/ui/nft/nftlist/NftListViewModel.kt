package com.aoztg.greengrim.presentation.ui.nft.nftlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.customview.NftSortType
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


data class NftListUiState(
    val nftList: List<UiNftItem> = emptyList(),
    val sortType: NftSortType = NftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class NftListEvents {
    data class NavigateToNftDetail(val id: Long) : NftListEvents()
    object ShowBottomSheet : NftListEvents()
    object ScrollToTop : NftListEvents()
    data class ShowSnackMessage(val msg: String) : NftListEvents()
    object ShowLoading : NftListEvents()
    object DismissLoading : NftListEvents()
    object NavigateToBack : NftListEvents()
}


@HiltViewModel
class NftListViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(NftListUiState())
    val uiState: StateFlow<NftListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftListEvents>()
    val events: SharedFlow<NftListEvents> = _events.asSharedFlow()


    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(NftListEvents.NavigateToNftDetail(id))
        }
    }

    private fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(NftListEvents.NavigateToBack)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(NftListEvents.ShowBottomSheet)
        }
    }
}