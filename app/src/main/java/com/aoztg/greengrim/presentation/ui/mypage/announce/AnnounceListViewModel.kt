package com.aoztg.greengrim.presentation.ui.mypage.announce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiAnnounceData
import com.aoztg.greengrim.presentation.ui.mypage.model.UiAnnounceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AnnounceListEvent {
    data class NavigateToAnnounceDetail(val id: Long) : AnnounceListEvent()
    object NavigateToBack: AnnounceListEvent()
}

@HiltViewModel
class AnnounceListViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {

    private val _announceList = MutableStateFlow<List<UiAnnounceData>>(emptyList())
    val announceList: StateFlow<List<UiAnnounceData>> = _announceList.asStateFlow()

    private val _event = MutableSharedFlow<AnnounceListEvent>()
    val event: SharedFlow<AnnounceListEvent> = _event.asSharedFlow()

    fun getAnnounceListData() {
        viewModelScope.launch {
            repository.getAnnounceList().let {
                when (it) {
                    is BaseState.Success -> {
                        _announceList.value = it.body.map { data ->
                            data.toUiAnnounceData(::navigateToAnnounceDetail)
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    private fun navigateToAnnounceDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(AnnounceListEvent.NavigateToAnnounceDetail(id))
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(AnnounceListEvent.NavigateToBack)
        }
    }
}