package com.aoztg.greengrim.presentation.ui.nft.nftcollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftCollectionItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionItem
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


data class NftCollectionUiState(
    val nftCollectionList: List<UiNftCollectionItem> = emptyList(),
    val curFilter: NftCollectionFilter = NftCollectionFilter.BASIC,
    val curCount: String = "",
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class NftCollectionEvent {
    data class NavigateToNftDetail(val id: Long) : NftCollectionEvent()
    data class ShowSnackMessage(val msg: String) : NftCollectionEvent()
    object ShowLoading : NftCollectionEvent()
    object DismissLoading : NftCollectionEvent()
    object NavigateToBack : NftCollectionEvent()
}


@HiltViewModel
class NftCollectionViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val _uiState = MutableStateFlow(NftCollectionUiState())
    val uiState: StateFlow<NftCollectionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftCollectionEvent>()
    val events: SharedFlow<NftCollectionEvent> = _events.asSharedFlow()

    private var basicCount = 0
    private var standardCount = 0
    private var premiumCount = 0

    fun setCount(basic: Int, standard: Int, premium: Int) {
        basicCount = basic
        standardCount = standard
        premiumCount = premium
    }

    fun changeFilter(filter: NftCollectionFilter) {
        _uiState.update { state ->
            state.copy(
                curFilter = filter,
                page = 0,
                hasNext = true,
            )
        }

        getNftCollectionList(NEW)
    }

    fun getNftCollectionList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {
                nftRepository.getNftCollection(
                    uiState.value.curFilter.text,
                    uiState.value.page,
                    10
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val newList =
                                it.body.result.map { data -> data.toUiNftCollectionItem() }
                            _uiState.update { state ->
                                state.copy(
                                    nftCollectionList = if (option == NEXT_PAGE) uiState.value.nftCollectionList + newList
                                    else newList,
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1,
                                    curCount = when (uiState.value.curFilter) {
                                        NftCollectionFilter.BASIC -> "$basicCount 개 남음"
                                        NftCollectionFilter.STANDARD -> "$standardCount 개 남음"
                                        NftCollectionFilter.PREMIUM -> "$premiumCount 개 남음"
                                    }
                                )
                            }
                        }

                        is BaseState.Error -> _events.emit(NftCollectionEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }


    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(NftCollectionEvent.NavigateToBack)
        }
    }
}

enum class NftCollectionFilter(val text: String) {
    BASIC("BASIC"),
    STANDARD("STANDARD"),
    PREMIUM("PREMIUM")
}