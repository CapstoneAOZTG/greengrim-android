package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.HomeRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiHomeInfo
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiHotChallenge
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiNftItem
import com.aoztg.greengrim.presentation.ui.home.model.UiHomeInfo
import com.aoztg.greengrim.presentation.ui.home.model.UiHotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.UiMoreActivity
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

data class HomeUiState(
    val uiHotChallengeList: List<UiHotChallenge> = emptyList(),
    val uiMoreActivityList: List<UiMoreActivity> = emptyList(),
    val uiHotNftList: List<UiNftItem> = emptyList(),
    val uiHomeInfo: UiHomeInfo = UiHomeInfo()
)

sealed class HomeEvents {
    data class NavigateToChallengeDetail(val id: Long) : HomeEvents()
    data class ShowToastMessage(val msg: String) : HomeEvents()
    data class ShowSnackMessage(val msg: String) : HomeEvents()
    object NavigateToAttendCheck : HomeEvents()
    object GoToGameActivity : HomeEvents()
    object ShowLoading : HomeEvents()
    object DismissLoading : HomeEvents()
    data class NavigateToNftDetail(val id: Long) : HomeEvents()
    object NavigateToNftList: HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun getHomeData() {
        viewModelScope.launch {
            _events.emit(HomeEvents.ShowLoading)
            getHomeInfo()
            getHotChallenges()
            getMoreActivity()
            getHotNft()
            _events.emit(HomeEvents.DismissLoading)
        }
    }

    private suspend fun getHomeInfo() {
        homeRepository.getHomeInfo().let {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            uiHomeInfo = it.body.toUiHomeInfo()
                        )
                    }
                }

                is BaseState.Error -> {
                    _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private suspend fun getHotChallenges() {
        homeRepository.getHotChallenges().let {
            when (it) {
                is BaseState.Success -> {
                    val uiModel = it.body.hotChallengeInfos.map { data ->
                        data.toUiHotChallenge(::navigateToChallengeDetail)
                    }
                    _uiState.update { state ->
                        state.copy(
                            uiHotChallengeList = uiModel
                        )
                    }
                }

                is BaseState.Error -> {
                    _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private suspend fun getMoreActivity() {
        _uiState.update { state ->
            state.copy(
                uiMoreActivityList = listOf(
                    UiMoreActivity(
                        R.drawable.icon_home_game,
                        "쓰레기 잡기",
                        "지금 플레이하면",
                        "+ 10 G",
                        ::goToGameActivity
                    ),
                    UiMoreActivity(
                        R.drawable.icon_home_attend_check,
                        "출석 체크",
                        "지금 상호 인증하면",
                        "+ 10 G",
                        ::navigateToAttendCheck
                    )
                )
            )
        }
    }

    private suspend fun getHotNft() {
        viewModelScope.launch {
            nftRepository.getHotNfts().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiHotNftList = it.body.homeNftInfos.map { data -> data.toUiNftItem(::navigateToNftDetail) }
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToChallengeDetail(id))
        }
    }

    private fun navigateToAttendCheck() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToAttendCheck)
        }
    }

    private fun goToGameActivity() {
        viewModelScope.launch {
            _events.emit(HomeEvents.GoToGameActivity)
        }
    }

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToNftDetail(id))
        }
    }

    fun navigateToNftList(){
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToNftList)
        }
    }

}