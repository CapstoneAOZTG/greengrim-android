package com.aoztg.greengrim.presentation.ui.info.mynft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiNftItem
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

data class MyNftUiState(
    val nftList: List<UiNftItem> = emptyList(),
    val sortType: GrimNftSortType = GrimNftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class MyNftEvents {
    data class NavigateToNftDetail(val id: Int) : MyNftEvents()
    object ShowBottomSheet : MyNftEvents()
    object ScrollToTop : MyNftEvents()
    data class ShowSnackMessage(val msg: String) : MyNftEvents()
    object ShowLoading : MyNftEvents()
    object DismissLoading : MyNftEvents()
    object NavigateToBack : MyNftEvents()
}

@HiltViewModel
class MyNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(MyNftUiState())
    val uiState: StateFlow<MyNftUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyNftEvents>()
    val events: SharedFlow<MyNftEvents> = _events.asSharedFlow()

    fun getGrimList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {
                _events.emit(MyNftEvents.ShowLoading)

                nftRepository.getMyNftList(
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

                        is BaseState.Error -> _events.emit(MyNftEvents.ShowSnackMessage(it.msg))
                    }
                    delay(500)
                    _events.emit(MyNftEvents.DismissLoading)
                }
            }
        }

    }

    private fun navigateToNftDetail(id: Int) {
        viewModelScope.launch {
            _events.emit(MyNftEvents.NavigateToNftDetail(id))
        }
    }

    private fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(MyNftEvents.NavigateToBack)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(MyNftEvents.ShowBottomSheet)
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