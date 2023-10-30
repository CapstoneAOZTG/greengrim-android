package com.aoztg.greengrim.presentation.ui.challenge.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CreateChallengeEvents {
    data class NavigateToCreateDetail(val category: String) : CreateChallengeEvents()
}

@HiltViewModel
class CreateChallengeViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow<List<ChallengeCategory>>(emptyList())
    val categories: StateFlow<List<ChallengeCategory>> = _categories.asStateFlow()

    private val _events = MutableSharedFlow<CreateChallengeEvents>()
    val events: SharedFlow<CreateChallengeEvents> = _events.asSharedFlow()

    private val selectedCategory = MutableStateFlow("")

    val isDataReady = combine(selectedCategory) { data ->
        data[0].isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun getCategoryList() {

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

    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    fun navigateToCreateChallengeDetail() {
        selectedCategory.value = ""
        viewModelScope.launch {
            _events.emit(CreateChallengeEvents.NavigateToCreateDetail(selectedCategory.value))
        }
    }
}