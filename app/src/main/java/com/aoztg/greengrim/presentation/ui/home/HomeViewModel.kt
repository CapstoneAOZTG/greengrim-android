package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _hotChallengeList = MutableLiveData<List<HomeUiModel>>()
    val hotChallengeList: LiveData<List<HomeUiModel>> = _hotChallengeList

    private val _moreActivityList = MutableLiveData<List<HomeUiModel>>()
    val moreActivityList: LiveData<List<HomeUiModel>> = _moreActivityList

    private val _hotNftList = MutableLiveData<List<HomeUiModel>>()
    val hotNftList: LiveData<List<HomeUiModel>> = _hotNftList

    fun getHomeList() {
        val dummyHomeList = listOf(
            HomeUiModel(
                chipList = listOf("줍킹", "남은 티켓 5개", "#줍다"),
                title = "쓰레기 줍고 지구 살리기"
            ),
            HomeUiModel(
                chipList = listOf("에코 제품 사용", "남은 티콋 6개", "#먹다"),
                title = "카페에서 항상 종이빨대!!"
            ),
            HomeUiModel(
                chipList = listOf("아아", "sdf", "asdf"),
                title = "전기자동차 인증팟!"
            )
        )
        _hotChallengeList.value = dummyHomeList

        val dummyMoreList = listOf(
            HomeUiModel(title = "쓰레기 잡기", textDetail = "지금 플레이하면", point = "+ 10 G"),
            HomeUiModel(title = "출석 체크", textDetail = "지금 상호 인증하면", point = "+ 10 G"),
        )
        _moreActivityList.value = dummyMoreList

        val dummyNftList = listOf(
            HomeUiModel(title = "장화 신은 고양이", userName = "Noah", carbon = "15.7 C"),
            HomeUiModel(title = "레인보우 빨대", userName = "Bora", carbon = "13.1 C"),
        )
        _hotNftList.value = dummyNftList

    }

}