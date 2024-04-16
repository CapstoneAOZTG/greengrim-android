package com.aoztg.greengrim.presentation.ui.mypage.mypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiMyPointInfo
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyPointInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MyPointUiState(
    val name: String = "",
    val totalPoint: String = "",
    val page : Int = 0,
    val hasNext : Boolean = true,
    val pointInfoList : List<UiMyPointInfo> = emptyList()
)

sealed class MyPointEvent{
    object NavigateToBack : MyPointEvent()
    data class ShowCustomSnack(val msg: String) : MyPointEvent()
}

@HiltViewModel
class MyPointViewModel @Inject constructor(
    private val repository : MemberRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MyPointUiState())
    val uiState : StateFlow<MyPointUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPointEvent>()
    val event: SharedFlow<MyPointEvent> = _event.asSharedFlow()

    fun setInfo(name: String, totalPoint: String){
        _uiState.update { state ->
            state.copy(
                name = name,
                totalPoint = totalPoint
            )
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(MyPointEvent.NavigateToBack)
        }
    }

    fun getMyPoint(){
        viewModelScope.launch {
            if(uiState.value.hasNext){
                repository.getMyPointInfo(
                    uiState.value.page,
                    20
                ).let{
                    when(it){
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    page = it.body.page + 1,
                                    hasNext = it.body.hasNext,
                                    pointInfoList = uiState.value.pointInfoList + it.body.result.map { data -> data.toUiMyPointInfo() }
                                )
                            }
                        }

                        is BaseState.Error -> _event.emit(MyPointEvent.ShowCustomSnack(it.msg))
                    }
                }
            }
        }
    }
}