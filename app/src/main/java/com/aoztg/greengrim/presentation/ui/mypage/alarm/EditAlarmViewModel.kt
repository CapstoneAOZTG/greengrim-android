package com.aoztg.greengrim.presentation.ui.mypage.alarm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class EditAlarmViewModel @Inject constructor() : ViewModel() {

    val pushAlarmState = MutableStateFlow(false)
    val advertisingState = MutableStateFlow(false)

}