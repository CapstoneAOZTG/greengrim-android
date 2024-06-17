package com.aoztg.greengrim.presentation.ui.nft

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.NftLikeRequest
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.mypage.MyPageEvent
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCategory
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
import com.aoztg.greengrim.presentation.util.Constants.TAG
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

data class NftUiState(
    val nftCategory: List<UiNftCategory> = emptyList(),
    val nftList: List<UiNftItem> = emptyList(),
    val sortType: NftSortType = NftSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
    val hasWallet: Boolean = false
)

sealed class NftEvent {
    data class NavigateToNftDetail(val id: Long) : NftEvent()
    object ShowBottomSheet : NftEvent()
    data class ShowSnackMessage(val msg: String) : NftEvent()
    object ShowLoading : NftEvent()
    object DismissLoading : NftEvent()
    data class NavigateToNftCollectionList(val select: String) : NftEvent()
    object NavigateToExchangeNft : NftEvent()
    object ShowHeartAnim: NftEvent()
}

@HiltViewModel
class NftViewModel @Inject constructor(
    private val nftRepository: NftRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    init {
        getNftCategory()
    }

    private val _uiState = MutableStateFlow(NftUiState())
    val uiState: StateFlow<NftUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftEvent>()
    val events: SharedFlow<NftEvent> = _events.asSharedFlow()

    private fun getNftCategory() {
        viewModelScope.launch {
            nftRepository.getNftCollectionCount().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nftCategory = listOf(
                                    UiNftCategory(
                                        img = R.drawable.icon_nft_basic,
                                        categoryName = "BASIC",
                                        count = it.body.basic,
                                        ::navigateToNftCollectionList
                                    ),
                                    UiNftCategory(
                                        img = R.drawable.icon_nft_standard,
                                        categoryName = "STANDARD",
                                        count = it.body.standard,
                                        ::navigateToNftCollectionList
                                    ),
                                    UiNftCategory(
                                        img = R.drawable.icon_nft_premium,
                                        categoryName = "PREMIUM",
                                        count = it.body.premium,
                                        ::navigateToNftCollectionList
                                    )
                                )
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(NftEvent.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun getNftList(option: Int) {
        if (uiState.value.hasNext) {
            viewModelScope.launch {
                nftRepository.getExchangedNftList(
                    uiState.value.page,
                    10,
                    uiState.value.sortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val newList = it.body.result.map { data ->
                                data.toUiNftItem(::navigateToNftDetail, ::clickLike)
                            }
                            _uiState.update { state ->
                                state.copy(
                                    nftList = if (option == NEXT_PAGE) uiState.value.nftList + newList else newList,
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1
                                )
                            }
                        }

                        is BaseState.Error -> _events.emit(NftEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }

    }

    fun getMyWalletInfo() {
        viewModelScope.launch {
            memberRepository.getMyWalletInfo().let {
                when (it) {
                    is BaseState.Success -> {

                        _uiState.update { state ->
                            state.copy(
                                hasWallet = it.body.existed
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(NftEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(NftEvent.NavigateToNftDetail(id))
        }
    }

    private fun clickLike(id: Long) {
        viewModelScope.launch {
            nftRepository.nftLike(
                NftLikeRequest(
                    id
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        var showAnim = false
                        _uiState.update { state ->
                            state.copy(
                                nftList = uiState.value.nftList.map { data ->
                                    if (data.id == id) {
                                        if(!data.isLiked){
                                            showAnim = true
                                        }
                                        data.copy(
                                            isLiked = !data.isLiked
                                        )
                                    } else {
                                        data
                                    }
                                }
                            )
                        }

                        if(showAnim){
                            _events.emit(NftEvent.ShowHeartAnim)
                        }

                    }

                    is BaseState.Error -> _events.emit(NftEvent.ShowSnackMessage(it.msg))
                }
            }
        }

    }

    fun navigateToNftCollectionList(select: String) {
        viewModelScope.launch {
            _events.emit(NftEvent.NavigateToNftCollectionList(select))
        }
    }

    fun navigateToExchangeNft() {
        viewModelScope.launch {
            _events.emit(NftEvent.NavigateToExchangeNft)
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

        getNftList(NEW)
    }

}

