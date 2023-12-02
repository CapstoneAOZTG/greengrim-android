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

sealed class ChallengeCategoryEvents{
    object NavigateToCreateChallenge : ChallengeCategoryEvents()
}

@HiltViewModel
class ChallengeCategoryViewModel @Inject constructor(): ViewModel() {

    private val _categories = MutableStateFlow<List<UiChallengeCategory>>(emptyList())
    val categories: StateFlow<List<UiChallengeCategory>> = _categories.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeCategoryEvents>()
    val events: SharedFlow<ChallengeCategoryEvents> = _events.asSharedFlow()

    fun getCategoryList(){

        _categories.value = listOf(
            UiChallengeCategory(R.drawable.icon_eco_bag, CategoryName.ECO_PRODUCT, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_trash_bag, CategoryName.PICK_UP_KING, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_plant, CategoryName.GROWING_PLANT, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_clothes, CategoryName.COOL_AND_HOT_LOOKING, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_light_bulb, CategoryName.DAILY, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_electric_car, CategoryName.ELECTRIC_CAR, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_bus, CategoryName.PUBLIC_TRANSPORTATION, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_thermometer, CategoryName.MAINTAINING_TEMPERATURE, "+ 10 G"),
            UiChallengeCategory(R.drawable.icon_recycle, CategoryName.RECYCLING, "+ 10 G"),
        )
    }

    fun navigateToCreateChallenge(){
        viewModelScope.launch{
            _events.emit(ChallengeCategoryEvents.NavigateToCreateChallenge)
        }
    }
}