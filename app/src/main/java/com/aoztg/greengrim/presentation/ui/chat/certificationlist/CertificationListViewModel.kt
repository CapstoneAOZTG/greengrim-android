package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.presentation.ui.chat.mapper.toCertificationListData
import com.aoztg.greengrim.presentation.ui.chat.mapper.toChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.chat.model.CertificationListItem
import com.aoztg.greengrim.presentation.ui.chat.model.ChallengeSimpleInfo
import com.aoztg.greengrim.presentation.ui.toHeaderText
import com.aoztg.greengrim.presentation.ui.toLocalDate
import com.aoztg.greengrim.presentation.ui.toText
import com.google.gson.Gson
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
    val challengeInfo: ChallengeSimpleInfo = ChallengeSimpleInfo(),
    val curMonthString: String = YearMonth.now().toText(),
    val curDateString: String = LocalDate.now().toHeaderText(),
    val curDate: LocalDate = LocalDate.now(),
    val certificationDateList: List<LocalDate> = emptyList(),
    val certificationList: List<CertificationListItem> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class CertificationListEvents {
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : CertificationListEvents()
    data class ShowToastMessage(val msg: String) : CertificationListEvents()
    object ShowCalendar: CertificationListEvents()
    data class NavigateToCertificationDetail(val certificationId: Int) : CertificationListEvents()
    object NavigateToBack: CertificationListEvents()
}

@HiltViewModel
class CertificationListViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository
) : ViewModel() {

    companion object {
        const val NEXT_PAGE = 0
        const val NEW_DATE = 1
    }

    private val _uiState = MutableStateFlow(CertificationListUiState())
    val uiState: StateFlow<CertificationListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CertificationListEvents>()
    val events: SharedFlow<CertificationListEvents> = _events.asSharedFlow()

    private var challengeId = -1
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

    fun getChallengeInfo(){
        viewModelScope.launch {
            val response = certificationRepository.getCertificationDetail(challengeId)

            if(response.isSuccessful){
                response.body()?.let { data ->
                    _uiState.update { state ->
                        state.copy(
                            challengeInfo = data.toChallengeSimpleInfo()
                        )
                    }
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(CertificationListEvents.ShowToastMessage(error.message))
            }
        }
    }


    fun getCertificationDate() {
        viewModelScope.launch {
            val response = certificationRepository.getCertificationDate(challengeId)

            if(response.isSuccessful){
                response.body()?.let { data ->
                    _uiState.update { state ->
                        state.copy(
                            certificationDateList = data.date.map { it.toLocalDate() }
                        )
                    }
                }
                _events.emit(CertificationListEvents.ShowCalendar)
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(CertificationListEvents.ShowToastMessage(error.message))
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

                val response = certificationRepository.getCertificationList(
                    challengeId,
                    _uiState.value.curDate.toString(),
                    _uiState.value.page,
                    20
                )

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val uiData = body.toCertificationListData(::navigateToCertificationDetail)
                        _uiState.update { state ->
                            state.copy(
                                certificationList = if (option == NEXT_PAGE) _uiState.value.certificationList + uiData.result else uiData.result,
                                hasNext = uiData.hasNext,
                                page = uiData.page + 1,
                            )
                        }
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    _events.emit(CertificationListEvents.ShowToastMessage(error.message))
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

    fun setChallengeId(id: Int){
        challengeId = id
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(CertificationListEvents.NavigateToBack)
        }
    }

    private fun navigateToCertificationDetail(certificationId: Int) {
        viewModelScope.launch {
            _events.emit(CertificationListEvents.NavigateToCertificationDetail(certificationId))
        }
    }
}