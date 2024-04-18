package com.aoztg.greengrim.presentation.ui.nft.nftcollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
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


data class NftListUiState(
    val nftCollectionList: List<UiNftCollectionItem> = emptyList(),
    val curFilter: NftCollectionFilter = NftCollectionFilter.BASIC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class NftListEvents {
    data class NavigateToNftDetail(val id: Long) : NftListEvents()
    data class ShowSnackMessage(val msg: String) : NftListEvents()
    object ShowLoading : NftListEvents()
    object DismissLoading : NftListEvents()
    object NavigateToBack : NftListEvents()
}


@HiltViewModel
class NftCollectionViewModel @Inject constructor(
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

    fun changeFilter(filter: NftCollectionFilter) {
        _uiState.update { state ->
            state.copy(
                curFilter = filter,
                page = 0,
                hasNext = true,
            )
        }

        when (filter) {
            NftCollectionFilter.BASIC -> {
                getNftCollectionList(NftCollectionFilter.BASIC.text)
            }

            NftCollectionFilter.STANDARD -> {
                getNftCollectionList(NftCollectionFilter.STANDARD.text)
            }

            NftCollectionFilter.PREMIUM -> {
                getNftCollectionList(NftCollectionFilter.PREMIUM.text)
            }
        }
    }

    fun getNftCollectionList(filter: String) {

        viewModelScope.launch {
            nftRepository
        }


    }


    private fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(NftListEvents.NavigateToBack)
        }
    }
}

enum class NftCollectionFilter(val text: String) {
    BASIC("BASIC"),
    STANDARD("STANDARD"),
    PREMIUM("PREMIUM")
}