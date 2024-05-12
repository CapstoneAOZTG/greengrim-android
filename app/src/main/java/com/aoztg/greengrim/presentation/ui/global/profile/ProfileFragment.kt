package com.aoztg.greengrim.presentation.ui.global.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentProfileBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.ChallengeFilterBottomSheet
import com.aoztg.greengrim.presentation.customview.CustomCalendar
import com.aoztg.greengrim.presentation.customview.NftFilterBottomSheet
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.mypage.adapter.MyCertificationAdapter
import com.aoztg.greengrim.presentation.ui.mypage.myprofile.MyProfileFragment
import com.aoztg.greengrim.presentation.ui.mypage.myprofile.MyProfileTempDate
import com.aoztg.greengrim.presentation.ui.mypage.myprofile.ProfileFilter
import com.aoztg.greengrim.presentation.ui.nft.adapter.NftItemAdapter
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.ui.toNftDetail
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ProfileViewModel by viewModels()
    private val popupLocation = IntArray(2)

    private var bottomScrollState = true

    private val args: ProfileFragmentArgs by navArgs()
    private val memberId by lazy { args.id }

    private lateinit var customCalendar: CustomCalendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setId(memberId)
        setBtnClickListener()
        setScrollEventListener()
        initCustomCalendar()
        initEventObserve()
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        binding.rvCertifications.adapter = MyCertificationAdapter()
        binding.rvNftList.adapter = NftItemAdapter()
        viewModel.getMemberInfo()
        viewModel.getMemberChallenge(NEXT_PAGE)
    }

    private fun setBtnClickListener() {
        binding.btnNextMonth.setOnClickListener {
            customCalendar.goToNextMonth()
        }

        binding.btnPreviousMonth.setOnClickListener {
            customCalendar.goToPreviousMonth()
        }
    }

    private fun setScrollEventListener() {

        binding.scrollView.setOnScrollChangeListener { v, _, scrollY, _, _ ->

            if (scrollY > binding.scrollView.getChildAt(0).measuredHeight - v.measuredHeight) {

                if (bottomScrollState) {
                    bottomScrollState = false
                    when (viewModel.uiState.value.curFilter) {
                        ProfileFilter.CHALLENGE -> {
                            viewModel.getMemberChallenge(MyProfileFragment.NEXT_PAGE)
                        }

                        ProfileFilter.CERTIFICATION -> {
                            viewModel.getCertificationList(MyProfileFragment.NEXT_PAGE)
                        }

                        ProfileFilter.NFT -> {
                            viewModel.getNftList(MyProfileFragment.NEXT_PAGE)
                        }
                    }
                }
            } else {
                bottomScrollState = true
            }
        }
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ProfileEvent.ShowChallengeFilterBottomSheet -> showChallengeFilterBottomSheet()
                    is ProfileEvent.ShowNftFilterBottomSheet -> showNftFilterBottomSheet()

                    is ProfileEvent.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )

                    is ProfileEvent.NavigateToCertificationDetail -> {
                        ProfileTempDate.setTempDate(customCalendar.selectedDate)
                        findNavController().toCertificationDetail(it.certificationId)
                    }

                    is ProfileEvent.NavigateToNftDetail -> findNavController().toNftDetail(it.id)

                    is ProfileEvent.ShowCalendar -> {
                        customCalendar.setDateWithDataList(
                            viewModel.uiState.value.certificationDateList
                        )
                    }

                    is ProfileEvent.ShowYearMonthPicker -> {
                        showYearMonthDialog(
                            requireContext(),
                            it.curYear,
                            it.curMonth,
                            ::yearMonthDatePickerConfirmListener
                        )
                    }

                    is ProfileEvent.DismissAccusationDialog -> {
                        dismissAccusation()
                        dismissOnePopup()
                    }
                    is ProfileEvent.ShowAccusationPopUp -> showPopup()
                    is ProfileEvent.InitCalendar -> {
                        // 캘린더 초기화 작업
                    }

                    is ProfileEvent.ShowSnackMessage -> showCustomSnack(binding.ivProfile, it.msg)
                    is ProfileEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is ProfileEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initCustomCalendar() {
        val tempDate = MyProfileTempDate.getTempDate()
        customCalendar = CustomCalendar(
            binding.calendarView,
            ::monthScrollListener,
            ::dateSelectListener,
            selectedMonth = tempDate.yearMonth,
            selectedDate = tempDate
        )
    }

    private fun showChallengeFilterBottomSheet() {
        ChallengeFilterBottomSheet(
            requireContext(),
            viewModel.uiState.value.challengeSortType
        ) { type ->
            viewModel.setChallengeSortType(type)
        }.show()
    }

    private fun showNftFilterBottomSheet() {
        NftFilterBottomSheet(requireContext(), viewModel.uiState.value.nftSortType) { type ->
            viewModel.setNftSortType(type)
        }.show()
    }

    private fun monthScrollListener(data: YearMonth) {
        viewModel.scrollMonth(data)
    }

    private fun dateSelectListener(data: LocalDate) {
        viewModel.selectDate(data)
    }

    private fun yearMonthDatePickerConfirmListener(year: Int, month: Int) {
        customCalendar.yearMonthDatePickerConfirmListener(year, month)
    }

    private fun showPopup() {
        val moreBtn = binding.btnMore
        moreBtn.getLocationOnScreen(popupLocation)
        val left = popupLocation[0] + moreBtn.left.toFloat()
        val top = popupLocation[1] + moreBtn.bottom.toFloat()
        showOnePopup(
            requireContext(),
            ::showAccusationDialog,
            left.toInt(),
            top.toInt()
        )
    }

    private fun showAccusationDialog() {
        showAccusation(requireContext(), "유저 신고") { accType, content ->
            viewModel.accusation(accType, content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissOnePopup()
    }

}