package com.aoztg.greengrim.presentation.ui.mypage.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.SortType
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiMyCertificationList
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyCertification
import com.aoztg.greengrim.presentation.ui.mypage.mycertification.MyCertificationEvents
import com.aoztg.greengrim.presentation.ui.mypage.mycertification.MyCertificationViewModel
import com.aoztg.greengrim.presentation.ui.mypage.mychallenge.MyChallengeViewModel
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

data class MyProfileUiState(
    val page: Int = 0,
    val hasNext: Boolean = true,
    val sortType: SortType = SortType.DESC,
    val curFilter: ProfileFilter = ProfileFilter.CHALLENGE,
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val curMonthString: String = YearMonth.now().toText(),
    val curDateString: String = LocalDate.now().toHeaderText(),
    val curDate: LocalDate = LocalDate.now(),
    val certificationDateList: List<LocalDate> = emptyList(),
    val certificationList: List<UiMyCertification> = emptyList(),
)

sealed class MyProfileEvent{
    data class NavigateToChallengeDetail(val id: Long) : MyProfileEvent()
    data class ShowToastMessage(val msg: String) : MyProfileEvent()
    data class ShowSnackMessage(val msg: String) : MyProfileEvent()
    object ShowChallengeFilterBottomSheet : MyProfileEvent()
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : MyProfileEvent()
    data class NavigateToCertificationDetail(val certificationId: Long) : MyProfileEvent()
    object ShowCalendar : MyProfileEvent()
    object InitCalendar : MyProfileEvent()
}

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val challengeRepository : ChallengeRepository,
    private val certificationRepository: CertificationRepository,
    private val nftRepository: NftRepository
): ViewModel() {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState : StateFlow<MyProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyProfileEvent>()
    val event: SharedFlow<MyProfileEvent> = _event.asSharedFlow()

    fun changeFilter(filter: ProfileFilter){
        _uiState.update { state ->
            state.copy(
                curFilter = filter,
                sortType = SortType.DESC,
                page = 0,
                hasNext = true
            )
        }

        when(filter){
            ProfileFilter.CHALLENGE -> {
                getMyChallenge(NEXT_PAGE)
            }

            ProfileFilter.CERTIFICATION -> {
                getCertificationList(NEW)
                getCertificationDate()
            }

            ProfileFilter.NFT -> {

            }
        }
    }

    fun setChallengeSortType(type: SortType){
        _uiState.value = uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )

        getMyChallenge(NEW)
    }

    fun getMyChallenge(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                challengeRepository.getMyChallenge(
                    _uiState.value.page,
                    20,
                    _uiState.value.sortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData = it.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = if (option == NEXT_PAGE) _uiState.value.uiChallengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(MyProfileEvent.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun showChallengeFilterBottomSheet() {
        viewModelScope.launch {
            _event.emit(MyProfileEvent.ShowChallengeFilterBottomSheet)
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(MyProfileEvent.NavigateToChallengeDetail(id))
        }
    }


    // 여기부터 Certification 로직

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
                curDate = date,
                curDateString = date.toHeaderText(),
                hasNext = true,
                page = 0
            )
        }

        getCertificationList(NEW)
    }

    fun getCertificationList(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                _uiState.update { state ->
                    state.copy(
                        page = state.page
                    )
                }

                certificationRepository.getMyCertificationList(
                    _uiState.value.curDate.toString(),
                    _uiState.value.page,
                    20
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData =
                                it.body.toUiMyCertificationList(::navigateToCertificationDetail)
                            _uiState.update { state ->
                                state.copy(
                                    certificationList = if (option == NEXT_PAGE) _uiState.value.certificationList + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(MyProfileEvent.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun getCertificationDate() {
        viewModelScope.launch {
            certificationRepository.getMyCertificationDate().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                certificationDateList = it.body.date.map { data -> data.toLocalDate() }
                            )
                        }
                        _event.emit(MyProfileEvent.ShowCalendar)
                    }

                    is BaseState.Error -> {
                        _event.emit(MyProfileEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }

    }

    private fun navigateToCertificationDetail(certificationId: Long) {
        viewModelScope.launch {
            _event.emit(MyProfileEvent.NavigateToCertificationDetail(certificationId))
        }
    }

    fun showYearMonthDatePicker() {
        viewModelScope.launch {
            _event.emit(
                MyProfileEvent.ShowYearMonthPicker(
                    curYear = curYear,
                    curMonth = curMonth
                )
            )
        }
    }

}

enum class ProfileFilter(){
    CHALLENGE,
    CERTIFICATION,
    NFT
}