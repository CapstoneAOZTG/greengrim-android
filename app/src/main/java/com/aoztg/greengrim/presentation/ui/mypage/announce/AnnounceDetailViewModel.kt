package com.aoztg.greengrim.presentation.ui.mypage.announce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiAnnounceDetailData
import com.aoztg.greengrim.presentation.ui.mypage.model.UiAnnounceDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnnounceDetailUiState(
    val data : UiAnnounceDetailData = UiAnnounceDetailData()
)

@HiltViewModel
class AnnounceDetailViewModel @Inject constructor(
    private val repository: MemberRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AnnounceDetailUiState())
    val uiState: StateFlow<AnnounceDetailUiState> = _uiState.asStateFlow()

    fun getAnnounceDetail(id: Long){
        viewModelScope.launch {
            repository.getAnnounceDetail(id).let{
                when(it){
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                data = it.body.toUiAnnounceDetailData()
                            )
                        }
                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }
}