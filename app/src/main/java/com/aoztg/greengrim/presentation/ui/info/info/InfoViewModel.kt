package com.aoztg.greengrim.presentation.ui.info.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class InfoEvents{
    object ShowBottomSheet : InfoEvents()
}

@HiltViewModel
class InfoViewModel @Inject constructor(): ViewModel() {

    private val _events = MutableSharedFlow<InfoEvents>()
    val events: SharedFlow<InfoEvents> = _events.asSharedFlow()


    fun showBottomSheet(){
        viewModelScope.launch{
            _events.emit(InfoEvents.ShowBottomSheet)
        }
    }
}
