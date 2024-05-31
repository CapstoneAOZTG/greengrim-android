package com.aoztg.greengrim.presentation.ui.challenge.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.challenge.model.CategoryName
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChallengeCategoryEvents {
    object NavigateToCreateChallenge : ChallengeCategoryEvents()
    object NavigateToSearchChallenge : ChallengeCategoryEvents()
    object NavigateToAlarmCheck: ChallengeCategoryEvents()
}

@HiltViewModel
class ChallengeCategoryViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow<List<UiChallengeCategory>>(emptyList())
    val categories: StateFlow<List<UiChallengeCategory>> = _categories.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeCategoryEvents>()
    val events: SharedFlow<ChallengeCategoryEvents> = _events.asSharedFlow()

    fun getCategoryList() {

        _categories.value = listOf(
            UiChallengeCategory(R.drawable.icon_eco_bag, CategoryName.ECO_PRODUCT),
            UiChallengeCategory(R.drawable.icon_trash_bag, CategoryName.PICK_UP_KING),
            UiChallengeCategory(R.drawable.icon_plant, CategoryName.GROWING_PLANT),
            UiChallengeCategory(R.drawable.icon_clothes, CategoryName.COOL_AND_HOT_LOOKING),
            UiChallengeCategory(R.drawable.icon_light_bulb, CategoryName.DAILY),
            UiChallengeCategory(R.drawable.icon_electric_car, CategoryName.ELECTRIC_CAR),
            UiChallengeCategory(R.drawable.icon_bus, CategoryName.PUBLIC_TRANSPORTATION),
            UiChallengeCategory(R.drawable.icon_thermometer, CategoryName.MAINTAINING_TEMPERATURE),
            UiChallengeCategory(R.drawable.icon_recycle, CategoryName.RECYCLING),
            UiChallengeCategory(R.drawable.icon_stairs, CategoryName.USING_STAIRS),
            UiChallengeCategory(R.drawable.icon_bicycle, CategoryName.USING_BICYCLE),
            UiChallengeCategory(R.drawable.icon_eco_badge, CategoryName.ECO_EVENT)
        )
    }

    fun navigateToCreateChallenge() {
        viewModelScope.launch {
            _events.emit(ChallengeCategoryEvents.NavigateToCreateChallenge)
        }
    }

    fun navigateToSearchChallenge(){
        viewModelScope.launch {
            _events.emit(ChallengeCategoryEvents.NavigateToSearchChallenge)
        }
    }

    fun navigateToAlarmCheck(){
        viewModelScope.launch {
            _events.emit(ChallengeCategoryEvents.NavigateToAlarmCheck)
        }
    }
}