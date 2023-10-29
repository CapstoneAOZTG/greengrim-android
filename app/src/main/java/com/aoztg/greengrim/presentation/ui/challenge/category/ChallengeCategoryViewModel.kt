package com.aoztg.greengrim.presentation.ui.challenge.category

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChallengeCategoryViewModel @Inject constructor(): ViewModel() {

    private val _categories = MutableStateFlow<List<ChallengeCategory>>(emptyList())
    val categories: StateFlow<List<ChallengeCategory>> = _categories.asStateFlow()

    fun getCategoryList(){
        
        _categories.value = listOf(
            ChallengeCategory(R.drawable.icon_eco_bag, "에코 제품 사용", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_trash_bag, "줍킹", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_plant, "식물 키우기", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_clothes, "쿨맵시 & 온맵시", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_light_bulb, "일상", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_electric_car, "전기차", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_bus, "대중교통 이용", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_thermometer, "적정 온도 유지", "+ 10 G"),
            ChallengeCategory(R.drawable.icon_recycle, "분리수거 라벨 제거", "+ 10 G"),
        )
    }

}