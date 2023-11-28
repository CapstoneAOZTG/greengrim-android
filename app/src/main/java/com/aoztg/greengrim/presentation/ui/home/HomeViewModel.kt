package com.aoztg.greengrim.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.HomeRepository
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.home.mapper.toHomeInfo
import com.aoztg.greengrim.presentation.ui.home.mapper.toHotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HomeInfo
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HotNft
import com.aoztg.greengrim.presentation.ui.home.model.MoreActivity
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

data class HomeUiState(
    val loading: LoadingState = LoadingState.Empty,
    val hotChallengeList: List<HotChallenge> = emptyList(),
    val moreActivityList: List<MoreActivity> = emptyList(),
    val hotNftList: List<HotNft> = emptyList(),
    val homeInfo: HomeInfo = HomeInfo()
)

sealed class HomeEvents{
    data class NavigateToChallengeDetail(val id : Int): HomeEvents()
    data class ShowToastMessage(val msg: String): HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun getHomeData(){
        getHomeInfo()
        getHotChallenges()
        getMoreActivity()
        getHotNft()
    }

    private fun getHomeInfo(){
        viewModelScope.async {
            val response = homeRepository.getHomeInfo()

            if(response.isSuccessful){
                response.body()?.let{
                    _uiState.update { state ->
                        state.copy(
                            homeInfo = it.toHomeInfo()
                        )
                    }
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(HomeEvents.ShowToastMessage(error.message))
            }
        }
    }

    private fun getHotChallenges() {
        viewModelScope.launch {
            val response = homeRepository.getHotChallenges()
            if(response.isSuccessful){
                response.body()?.let{
                    val uiModel = it.hotChallengeInfos.map{ data ->
                        data.toHotChallenge(::navigateToChallengeDetail)
                    }
                    _uiState.update { state ->
                        state.copy(
                            hotChallengeList = uiModel
                        )
                    }
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(HomeEvents.ShowToastMessage(error.message))
            }
        }
    }

    private fun getMoreActivity(){
        _uiState.update { state ->
            state.copy(
                moreActivityList = listOf(
                    MoreActivity("", "쓰레기 잡기", "지금 플레이하면", "+ 10 G"),
                    MoreActivity("", "출석 체크", "지금 상호 인증하면", "+ 10 G")
                )
            )
        }
    }

    private fun getHotNft(){
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