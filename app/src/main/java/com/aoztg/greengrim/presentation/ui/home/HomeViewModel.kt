package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiHotChallenge
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiHotNftItem
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiRecentIssue
import com.aoztg.greengrim.presentation.ui.home.model.UiHotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.UiHotNftItem
import com.aoztg.greengrim.presentation.ui.home.model.UiHomeRecentIssue
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
    val uiHomeRecentIssueList: List<UiHomeRecentIssue> = emptyList(),
    val uiHotNftList: List<UiHotNftItem> = emptyList(),
    val nickName: String = "",
    val carbonReduction: String = "",
    val greenPoint: String = "",
    val eventName: String = "",
    val eventImg: String = "",
    val eventUrl: String = ""
)

sealed class HomeEvents {
    data class NavigateToChallengeDetail(val id: Long) : HomeEvents()
    data class ShowToastMessage(val msg: String) : HomeEvents()
    data class ShowSnackMessage(val msg: String) : HomeEvents()
    data class NavigateToIssueDetail(val id: Long) : HomeEvents()
    object ShowLoading : HomeEvents()
    object DismissLoading : HomeEvents()
    data class NavigateToNftDetail(val id: Long) : HomeEvents()
    object NavigateToNftList : HomeEvents()
    object NavigateToHotChallengeList : HomeEvents()
    object NavigateToAlarmCheck : HomeEvents()
    object NavigateToIssueList: HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val challengeRepository: ChallengeRepository,
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun getHomeData() {
        viewModelScope.launch {
            _events.emit(HomeEvents.ShowLoading)
            getHomeMyInfo()
            getHomeEvent()
            getHotChallenges()
            getRecentIssues()
            getHotNft()
            _events.emit(HomeEvents.DismissLoading)
        }
    }

    private fun getHomeMyInfo() {
        viewModelScope.launch {
            memberRepository.getHomeMyInfo().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nickName = it.body.nickName,
                                carbonReduction = it.body.carbonReduction,
                                greenPoint = it.body.point
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun getHomeEvent() {
        viewModelScope.launch {
            memberRepository.getEvent().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                eventImg = it.body.imgUrl,
                                eventName = it.body.title,
                                eventUrl = it.body.url
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun getHotChallenges() {
        viewModelScope.launch {
            challengeRepository.getHotChallenges().let {
                when (it) {
                    is BaseState.Success -> {
                        val uiModel = it.body.challengeInfos.map { data ->
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

    }

    private fun getRecentIssues() {
        viewModelScope.launch {
            memberRepository.getHomeIssues().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiHomeRecentIssueList = it.body.issueInfos.map { data ->
                                    data.toUiRecentIssue(
                                        ::navigateToIssueDetail
                                    )
                                }
                            )
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }

    }

    private fun getHotNft() {
        viewModelScope.launch {
            nftRepository.getHotNft().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiHotNftList = it.body.result.map { data -> data.toUiHotNftItem(::navigateToNftDetail) }
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun navigateToIssueDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToIssueDetail(id))
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToChallengeDetail(id))
        }
    }

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToNftDetail(id))
        }
    }

    fun navigateToNftList() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToNftList)
        }
    }

    fun navigateToAlarmCheck() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToAlarmCheck)
        }
    }

    fun navigateToHotChallengeList() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToHotChallengeList)
        }
    }

    fun navigateToIssueList(){
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToIssueList)
        }
    }

}