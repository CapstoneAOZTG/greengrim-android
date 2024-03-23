package com.aoztg.greengrim.presentation.ui.nft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiNftItem
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiGrimItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiGrimItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
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

data class MarketUiState(
    val hotNftList: List<UiNftItem> = emptyList(),
    val grimList: List<UiGrimItem> = emptyList(),
    val sortType: GrimNftSortType = GrimNftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class MarketEvents {
    data class NavigateToGrimDetail(val id: Long) : MarketEvents()
    data class NavigateToNftDetail(val id: Long) : MarketEvents()
    object NavigateToCreateNftOrPaint : MarketEvents()
    object ShowBottomSheet : MarketEvents()
    object ScrollToTop : MarketEvents()
    data class ShowSnackMessage(val msg: String) : MarketEvents()
    object ShowLoading : MarketEvents()
    object DismissLoading : MarketEvents()
    object NavigateToNftList : MarketEvents()
}

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MarketEvents>()
    val events: SharedFlow<MarketEvents> = _events.asSharedFlow()

    fun getGrimList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {
                _events.emit(MarketEvents.ShowLoading)

                nftRepository.getGrimList(
                    uiState.value.page,
                    20,
                    uiState.value.sortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData =
                                it.body.result.map { data -> data.toUiGrimItem(::navigateToGrimDetail) }
                            _uiState.update { state ->
                                state.copy(
                                    grimList = if (option == ChallengeListViewModel.ORIGINAL) uiState.value.grimList + uiData else uiData,
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> _events.emit(MarketEvents.ShowSnackMessage(it.msg))
                    }
                    delay(500)
                    _events.emit(MarketEvents.DismissLoading)
                }
            }
        }
    }

    fun getHotNft() {
        viewModelScope.launch {
            nftRepository.getHotNfts().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                hotNftList = it.body.homeNftInfos.map { data -> data.toUiNftItem(::navigateToNftDetail) }
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(MarketEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(MarketEvents.NavigateToNftDetail(id))
        }
    }

    private fun navigateToGrimDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(MarketEvents.NavigateToGrimDetail(id))
        }
    }

    fun navigateToCreateNftOrPaint() {
        viewModelScope.launch {
            _events.emit(MarketEvents.NavigateToCreateNftOrPaint)
        }
    }

    fun navigateToNftList(){
        viewModelScope.launch {
            _events.emit(MarketEvents.NavigateToNftList)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(MarketEvents.ShowBottomSheet)
        }
    }

    fun setSortType(type: GrimNftSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )
        getGrimList(SORT)
    }

}

enum class GrimNftSortType(val text: String, val value: String) {
    DESC("최신순", "DESC"),
    ASC("오래된 순", "ASC"),
}