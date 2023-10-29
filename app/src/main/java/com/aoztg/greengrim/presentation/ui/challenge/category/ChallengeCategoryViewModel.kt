package com.aoztg.greengrim.presentation.ui.challenge.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChallengeCategoryEvents{
    data class NavigateToChallengeList(val title: String) : ChallengeCategoryEvents()
}

@HiltViewModel
class ChallengeCategoryViewModel @Inject constructor(): ViewModel() {

    private val _categories = MutableStateFlow<List<ChallengeCategory>>(emptyList())
    val categories: StateFlow<List<ChallengeCategory>> = _categories.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeCategoryEvents>()
    val events: SharedFlow<ChallengeCategoryEvents> = _events.asSharedFlow()

    fun getCategoryList(){

        _categories.value = listOf(
            ChallengeCategory(R.drawable.icon_eco_bag, "에코 제품 사용", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_trash_bag, "줍킹", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_plant, "식물 키우기", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_clothes, "쿨맵시 & 온맵시", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_light_bulb, "일상", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_electric_car, "전기차", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_bus, "대중교통 이용", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_thermometer, "적정 온도 유지", "+ 10 G", ::navigateToChallengeList),
            ChallengeCategory(R.drawable.icon_recycle, "분리수거 라벨 제거", "+ 10 G", ::navigateToChallengeList),
        )
    }

    private fun navigateToChallengeList(title: String){
        viewModelScope.launch{
            _events.emit(ChallengeCategoryEvents.NavigateToChallengeList(title))
        }
    }
}