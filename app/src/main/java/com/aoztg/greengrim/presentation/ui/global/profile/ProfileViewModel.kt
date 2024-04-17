package com.aoztg.greengrim.presentation.ui.global.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.customview.ChallengeSortType
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiSimpleProfile
import com.aoztg.greengrim.presentation.ui.global.model.UiSimpleProfileData
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftItem
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiMyCertificationList
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyCertification
import com.aoztg.greengrim.presentation.ui.mypage.myprofile.ProfileFilter
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
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


data class ProfileUiState(
    val page: Int = 0,
    val hasNext: Boolean = true,
    val uiSimpleProfileInfo: UiSimpleProfileData = UiSimpleProfileData(),
    val challengeSortType: ChallengeSortType = ChallengeSortType.DESC,
    val nftSortType: NftSortType = NftSortType.DESC,
    val curFilter: ProfileFilter = ProfileFilter.CHALLENGE,
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val curMonthString: String = YearMonth.now().toText(),
    val curDateString: String = LocalDate.now().toHeaderText(),
    val curDate: LocalDate = LocalDate.now(),
    val certificationDateList: List<LocalDate> = emptyList(),
    val certificationList: List<UiMyCertification> = emptyList(),
    val nftList: List<UiNftItem> = emptyList(),
)

sealed class ProfileEvent {
    data class NavigateToChallengeDetail(val id: Long) : ProfileEvent()
    data class NavigateToNftDetail(val id: Long) : ProfileEvent()
    data class NavigateToCertificationDetail(val certificationId: Long) : ProfileEvent()
    object NavigateToBack : ProfileEvent()
    object ShowChallengeFilterBottomSheet : ProfileEvent()
    object ShowNftFilterBottomSheet : ProfileEvent()
    object ShowAccusationPopUp : ProfileEvent()
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : ProfileEvent()
    object ShowCalendar : ProfileEvent()
    object InitCalendar : ProfileEvent()
    data class ShowToastMessage(val msg: String) : ProfileEvent()
    data class ShowSnackMessage(val msg: String) : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val certificationRepository: CertificationRepository,
    private val nftRepository: NftRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event: SharedFlow<ProfileEvent> = _event.asSharedFlow()

    var memberId = -1L

    fun setId(id: Long) {
        memberId = id
    }

    fun changeFilter(filter: ProfileFilter) {
        _uiState.update { state ->
            state.copy(
                curFilter = filter,
                challengeSortType = ChallengeSortType.DESC,
                page = 0,
                hasNext = true
            )
        }

        when (filter) {
            ProfileFilter.CHALLENGE -> {
                getMyChallenge(NEXT_PAGE)
            }

            ProfileFilter.CERTIFICATION -> {
                getCertificationList(NEW)
                getCertificationDate()
            }

            ProfileFilter.NFT -> {
                getNftList(NEW)
            }
        }
    }

    fun setChallengeSortType(type: ChallengeSortType) {
        _uiState.value = uiState.value.copy(
            hasNext = true,
            challengeSortType = type,
            page = 0
        )

        getMyChallenge(NEW)
    }

    fun getMyInfo() {
        viewModelScope.launch {
            memberRepository.getMemberInfo(memberId).let {
                when (it) {
                    is BaseState.Success -> {
                        val newBody = it.body.toUiSimpleProfile()
                        if (newBody != uiState.value.uiSimpleProfileInfo) {
                            _uiState.update { state ->
                                state.copy(
                                    uiSimpleProfileInfo = newBody
                                )
                            }
                        }
                    }

                    is BaseState.Error -> {
                        _event.emit(ProfileEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun getMyChallenge(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                challengeRepository.getMemberChallengeList(
                    memberId,
                    _uiState.value.page,
                    20,
                    _uiState.value.challengeSortType.value
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
                            _event.emit(ProfileEvent.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun showChallengeFilterBottomSheet() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.ShowChallengeFilterBottomSheet)
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToChallengeDetail(id))
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

                certificationRepository.getMemberCertificationList(
                    memberId,
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
                            _event.emit(ProfileEvent.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun getCertificationDate() {
        viewModelScope.launch {
            certificationRepository.getMemberCertificationDate(memberId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                certificationDateList = it.body.date.map { data -> data.toLocalDate() }
                            )
                        }
                        _event.emit(ProfileEvent.ShowCalendar)
                    }

                    is BaseState.Error -> {
                        _event.emit(ProfileEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }

    }

    private fun navigateToCertificationDetail(certificationId: Long) {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToCertificationDetail(certificationId))
        }
    }

    fun showYearMonthDatePicker() {
        viewModelScope.launch {
            _event.emit(
                ProfileEvent.ShowYearMonthPicker(
                    curYear = curYear,
                    curMonth = curMonth
                )
            )
        }
    }

    // 여기부터 Nft 로직

    fun setNftSortType(type: NftSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            nftSortType = type,
            page = 0
        )
        getNftList(NEW)
    }

    fun getNftList(option: Int) {
        if (uiState.value.hasNext) {
            viewModelScope.launch {

                nftRepository.getMemberNftList(
                    memberId,
                    uiState.value.page,
                    20,
                    uiState.value.nftSortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData =
                                it.body.result.map { data -> data.toUiNftItem(::navigateToNftDetail) }
                            _uiState.update { state ->
                                state.copy(
                                    nftList = if (option == ChallengeListViewModel.ORIGINAL) uiState.value.nftList + uiData else uiData,
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> _event.emit(ProfileEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun showNftFilterBottomSheet() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.ShowNftFilterBottomSheet)
        }
    }

    private fun navigateToNftDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToNftDetail(id))
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.NavigateToBack)
        }
    }

    fun showAccusationPopUp() {
        viewModelScope.launch {
            _event.emit(ProfileEvent.ShowAccusationPopUp)
        }
    }

}