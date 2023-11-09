package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.HomeRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.home.mapper.toHotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge
import com.aoztg.greengrim.presentation.ui.home.model.HotNft
import com.aoztg.greengrim.presentation.ui.home.model.MoreActivity
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val loading: LoadingState = LoadingState.Empty,
    val apiState: BaseState = BaseState.Empty
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _hotChallengeList = MutableStateFlow<List<HotChallenge>>(emptyList())
    val hotChallengeList: StateFlow<List<HotChallenge>> = _hotChallengeList.asStateFlow()

    private val _moreActivityList = MutableStateFlow<List<MoreActivity>>(emptyList())
    val moreActivityList: StateFlow<List<MoreActivity>> = _moreActivityList.asStateFlow()

    private val _hotNftList = MutableStateFlow<List<HotNft>>(emptyList())
    val hotNftList: StateFlow<List<HotNft>> = _hotNftList.asStateFlow()

    fun getHomeList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            val response = homeRepository.getHotChallenges()

            if(response.isSuccessful){
                response.body()?.let{
                    val uiModel = it.hotChallengeInfos.map{ data ->
                        data.toHotChallenge()
                    }
                    _hotChallengeList.value = uiModel
                }
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)

                _uiState.update { state ->
                    state.copy(
                        apiState = BaseState.Error(error.message)
                    )
                }
            }

            _moreActivityList.value = listOf(
                MoreActivity("", "쓰레기 잡기", "지금 플레이하면", "+ 10 G"),
                MoreActivity("", "출석 체크", "지금 상호 인증하면", "+ 10 G")
            )

            _hotNftList.value = listOf(
                HotNft("", "장화 신은 고양이", "", "Noah", "15.7 C"),
                HotNft("", "레인보우 빨대", "", "Bora", "13.1 C")
            )

            delay(1000)

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(false)
                )
            }
        }

    }

}