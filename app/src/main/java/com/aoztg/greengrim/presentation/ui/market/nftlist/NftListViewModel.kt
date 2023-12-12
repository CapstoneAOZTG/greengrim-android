package com.aoztg.greengrim.presentation.ui.market.nftlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiNftItem
import com.aoztg.greengrim.presentation.ui.info.mynft.MyNftEvents
import com.aoztg.greengrim.presentation.ui.market.GrimNftSortType
import com.aoztg.greengrim.presentation.ui.market.MarketViewModel
import com.aoztg.greengrim.presentation.ui.market.model.UiNftItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    val nftList: List<UiNftItem> = emptyList(),
    val sortType: GrimNftSortType = GrimNftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class NftListEvents {
    data class NavigateToNftDetail(val id: Int) : NftListEvents()
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

    fun getGrimList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {
                _events.emit(NftListEvents.ShowLoading)

                nftRepository.getMoreNft(
                    uiState.value.page,
                    20,
                    uiState.value.sortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData =
                                it.body.result.map { data -> data.toUiNftItem(::navigateToNftDetail) }
                            _uiState.update { state ->
                                state.copy(
                                    nftList = if (option == ChallengeListViewModel.ORIGINAL) uiState.value.nftList + uiData else uiData,
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> _events.emit(NftListEvents.ShowSnackMessage(it.msg))
                    }
                    delay(500)
                    _events.emit(NftListEvents.DismissLoading)
                }
            }
        }

    }

    private fun navigateToNftDetail(id: Int) {
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

    fun setSortType(type: GrimNftSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )
        getGrimList(MarketViewModel.SORT)
    }
}