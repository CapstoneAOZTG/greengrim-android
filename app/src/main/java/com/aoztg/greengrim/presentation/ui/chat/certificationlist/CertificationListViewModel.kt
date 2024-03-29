package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiCertificationList
import com.aoztg.greengrim.presentation.ui.chat.mapper.toUiChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.chat.model.UiCertificationItem
import com.aoztg.greengrim.presentation.ui.chat.model.UiChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.toHeaderText
import com.aoztg.greengrim.presentation.ui.toLocalDate
import com.aoztg.greengrim.presentation.ui.toText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


data class CertificationListUiState(
    val challengeInfo: UiChallengeSimpleInfo = UiChallengeSimpleInfo(),
    val curMonthString: String = YearMonth.now().toText(),
    val curDateString: String = LocalDate.now().toHeaderText(),
    val curDate: LocalDate = LocalDate.now(),
    val certificationDateList: List<LocalDate> = emptyList(),
    val certificationList: List<UiCertificationItem> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class CertificationListEvents {
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : CertificationListEvents()
    data class ShowToastMessage(val msg: String) : CertificationListEvents()
    object ShowCalendar : CertificationListEvents()
    data class NavigateToCertificationDetail(val certificationId: Long) : CertificationListEvents()
    object NavigateToBack : CertificationListEvents()
    data class ShowSnackMessage(val msg: String) : CertificationListEvents()
}

@HiltViewModel
class CertificationListViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    companion object {
        const val NEXT_PAGE = 0
        const val NEW_DATE = 1
    }

    private val _uiState = MutableStateFlow(CertificationListUiState())
    val uiState: StateFlow<CertificationListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CertificationListEvents>()
    val events: SharedFlow<CertificationListEvents> = _events.asSharedFlow()

    private var challengeId = -1L
    private var curYear = 1980
    private var curMonth = 1

    fun scrollMonth(date: YearMonth) {
        _uiState.update { state ->
            state.copy(
                curMonthString = date.toText()
            )
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                curDateString = date.toHeaderText(),
                curDate = date,
                hasNext = true,
                page = 0
            )
        }

        getCertificationList(NEW_DATE)
    }

    fun getChallengeInfo() {
        viewModelScope.launch {
            challengeRepository.getChallengeDetail(challengeId)
                .let {
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    challengeInfo = it.body.toUiChallengeSimpleInfo()
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _events.emit(CertificationListEvents.ShowSnackMessage(it.msg))
                        }
                    }
                }
        }
    }


    fun getCertificationDate() {
        viewModelScope.launch {
            certificationRepository.getCertificationDate(challengeId)
                .let {
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    certificationDateList = it.body.date.map { data -> data.toLocalDate() }
                                )
                            }
                            _events.emit(CertificationListEvents.ShowCalendar)
                        }

                        is BaseState.Error -> {
                            _events.emit(CertificationListEvents.ShowSnackMessage(it.msg))
                        }
                    }
                }
        }
    }

    fun getCertificationList(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                _uiState.update { state ->
                    state.copy(
                        page = state.page
                    )
                }

                certificationRepository.getCertificationList(
                    challengeId,
                    _uiState.value.curDate.toString(),
                    _uiState.value.page,
                    20
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData =
                                it.body.toUiCertificationList(::navigateToCertificationDetail)
                            _uiState.update { state ->
                                state.copy(
                                    certificationList = if (option == NEXT_PAGE) _uiState.value.certificationList + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _events.emit(CertificationListEvents.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun showYearMonthDatePicker() {
        viewModelScope.launch {
            _events.emit(
                CertificationListEvents.ShowYearMonthPicker(
                    curYear = curYear,
                    curMonth = curMonth
                )
            )
        }
    }

    fun setChallengeId(id: Long) {
        challengeId = id
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(CertificationListEvents.NavigateToBack)
        }
    }

    private fun navigateToCertificationDetail(certificationId: Long) {
        viewModelScope.launch {
            _events.emit(CertificationListEvents.NavigateToCertificationDetail(certificationId))
        }
    }
}