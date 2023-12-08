package com.aoztg.greengrim.presentation.ui.info.mypaint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.market.GrimNftSortType
import com.aoztg.greengrim.presentation.ui.market.MarketViewModel
import com.aoztg.greengrim.presentation.ui.market.mapper.toUiGrimItem
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimItem
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

data class MyPaintUiState(
    val grimList: List<UiGrimItem> = emptyList(),
    val sortType: GrimNftSortType = GrimNftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class MyPaintEvents {
    data class NavigateToGrimDetail(val id: Int) : MyPaintEvents()
    object ShowBottomSheet : MyPaintEvents()
    object ScrollToTop : MyPaintEvents()
    data class ShowSnackMessage(val msg: String) : MyPaintEvents()
    object ShowLoading : MyPaintEvents()
    object DismissLoading : MyPaintEvents()
    object NavigateToBack : MyPaintEvents()
}

@HiltViewModel
class MyPaintViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(MyPaintUiState())
    val uiState: StateFlow<MyPaintUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyPaintEvents>()
    val events: SharedFlow<MyPaintEvents> = _events.asSharedFlow()

    fun getGrimList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {
                _events.emit(MyPaintEvents.ShowLoading)

                nftRepository.getMyGrimList(
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

                        is BaseState.Error -> _events.emit(MyPaintEvents.ShowSnackMessage(it.msg))
                    }
                    delay(500)
                    _events.emit(MyPaintEvents.DismissLoading)
                }
            }
        }

    }

    private fun navigateToGrimDetail(id: Int) {
        viewModelScope.launch {
            _events.emit(MyPaintEvents.NavigateToGrimDetail(id))
        }
    }

    private fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(MyPaintEvents.NavigateToBack)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(MyPaintEvents.ShowBottomSheet)
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