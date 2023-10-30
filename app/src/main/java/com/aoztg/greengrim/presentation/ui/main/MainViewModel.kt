package com.aoztg.greengrim.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.main.event.MainEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _eventFlow : MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow : SharedFlow<MainEvent> = _eventFlow


    fun showBNV(){
        viewModelScope.launch{
            _eventFlow.emit(MainEvent.ShowBottomNav)
        }
    }

    fun hideBNV(){
        viewModelScope.launch{
            _eventFlow.emit(MainEvent.HideBottomNav)
        }
    }

}