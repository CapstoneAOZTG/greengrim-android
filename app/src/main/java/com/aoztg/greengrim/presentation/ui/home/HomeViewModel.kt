package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.HomeRepository
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.home.mapper.toHomeInfo
import com.aoztg.greengrim.presentation.ui.home.mapper.toHotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HomeInfo
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HotNft
import com.aoztg.greengrim.presentation.ui.home.model.MoreActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    val loading: LoadingState = LoadingState.Empty,
    val hotChallengeList: List<HotChallenge> = emptyList(),
    val moreActivityList: List<MoreActivity> = emptyList(),
    val hotNftList: List<HotNft> = emptyList(),
    val homeInfo: HomeInfo = HomeInfo()
)

sealed class HomeEvents {
    data class NavigateToChallengeDetail(val id: Int) : HomeEvents()
    data class ShowToastMessage(val msg: String) : HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun getHomeData() {
        getHomeInfo()
        getHotChallenges()
        getMoreActivity()
        getHotNft()
    }

    private fun getHomeInfo() {
        viewModelScope.async {
            homeRepository.getHomeInfo().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                homeInfo = it.body.toHomeInfo()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(HomeEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun getHotChallenges() {
        viewModelScope.launch {
            homeRepository.getHotChallenges().let {
                when (it) {
                    is BaseState.Success -> {
                        val uiModel = it.body.hotChallengeInfos.map { data ->
                            data.toHotChallenge(::navigateToChallengeDetail)
                        }
                        _uiState.update { state ->
                            state.copy(
                                hotChallengeList = uiModel
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(HomeEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun getMoreActivity() {
        _uiState.update { state ->
            state.copy(
                moreActivityList = listOf(
                    MoreActivity("", "쓰레기 잡기", "지금 플레이하면", "+ 10 G"),
                    MoreActivity("", "출석 체크", "지금 상호 인증하면", "+ 10 G")
                )
            )
        }
    }

    private fun getHotNft() {
        _uiState.update { state ->
            state.copy(
                hotNftList = listOf(
                    HotNft("", "장화 신은 고양이", "", "Noah", "15.7 C"),
                    HotNft("", "레인보우 빨대", "", "Bora", "13.1 C")
                )
            )
        }
    }

    private fun navigateToChallengeDetail(id: Int) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToChallengeDetail(id))
        }
    }

}