package com.aoztg.greengrim.presentation.ui.mypage.myprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyProfileBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.ChallengeFilterBottomSheet
import com.aoztg.greengrim.presentation.customview.ChallengeSortType
import com.aoztg.greengrim.presentation.customview.CustomCalendar
import com.aoztg.greengrim.presentation.customview.NftFilterBottomSheet
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.mypage.adapter.MyCertificationAdapter
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.ui.toNftDetail
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth


@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val parentViewModel : MainViewModel by activityViewModels()
    private val viewModel : MyProfileViewModel by viewModels()

    private lateinit var customCalendar: CustomCalendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        setBtnClickListener()
        setScrollEventListener()
        initCustomCalendar()
        initEventObserve()
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        binding.rvCertifications.adapter = MyCertificationAdapter()
        viewModel.getMyInfo()
        viewModel.getMyChallenge(NEXT_PAGE)
    }

    private fun setBtnClickListener(){
        binding.btnNextMonth.setOnClickListener{
            customCalendar.goToNextMonth()
        }

        binding.btnPreviousMonth.setOnClickListener{
            customCalendar.goToPreviousMonth()
        }
    }

    private fun setScrollEventListener() {

        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                when(viewModel.uiState.value.curFilter){
                    ProfileFilter.CHALLENGE -> {
                        viewModel.getMyChallenge(NEXT_PAGE)
                    }

                    ProfileFilter.CERTIFICATION -> {
                        viewModel.getCertificationList(NEXT_PAGE)
                    }

                    ProfileFilter.NFT -> {
                        viewModel.getNftList(NEXT_PAGE)
                    }
                }
            }
        }
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is MyProfileEvent.ShowChallengeFilterBottomSheet -> showChallengeFilterBottomSheet()
                    is MyProfileEvent.ShowNftFilterBottomSheet -> showNftFilterBottomSheet()

                    is MyProfileEvent.NavigateToChallengeDetail -> findNavController().toChallengeDetail(it.id)
                    is MyProfileEvent.NavigateToCertificationDetail -> {
                        MyProfileTempDate.setTempDate(customCalendar.selectedDate)
                        findNavController().toCertificationDetail(it.certificationId)
                    }
                    is MyProfileEvent.NavigateToNftDetail -> findNavController().toNftDetail(it.id)

                    is MyProfileEvent.ShowCalendar -> {
                        customCalendar.setDateWithDataList(
                            viewModel.uiState.value.certificationDateList
                        )
                    }

                    is MyProfileEvent.ShowYearMonthPicker -> {
                        showYearMonthDialog(
                            requireContext(),
                            it.curYear,
                            it.curMonth,
                            ::yearMonthDatePickerConfirmListener
                        )
                    }
                    is MyProfileEvent.InitCalendar -> {
                        // 캘린더 초기화 작업
                    }
                    is MyProfileEvent.ShowSnackMessage -> showCustomSnack(binding.ivProfile, it.msg)
                    is MyProfileEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is MyProfileEvent.NavigateToEditProfile -> findNavController().toEditProfile()
                    is MyProfileEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initCustomCalendar(){
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
        ChallengeFilterBottomSheet(requireContext(), viewModel.uiState.value.challengeSortType) { type ->
            viewModel.setChallengeSortType(type)
        }.show()
    }

    private fun showNftFilterBottomSheet() {
        NftFilterBottomSheet(requireContext(), viewModel.uiState.value.nftSortType) { type ->
            viewModel.setNftSortType(type)
        }.show()
    }

    private fun monthScrollListener(data: YearMonth){
        viewModel.scrollMonth(data)
    }

    private fun dateSelectListener(data: LocalDate){
        viewModel.selectDate(data)
    }

    private fun yearMonthDatePickerConfirmListener(year:Int, month:Int){
        customCalendar.yearMonthDatePickerConfirmListener(year, month)
    }

    private fun NavController.toEditProfile(){
        val action = MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment()
        navigate(action)
    }


}