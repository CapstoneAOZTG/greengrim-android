package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCertificationListBottomSheetBinding
import com.aoztg.greengrim.presentation.customview.CustomCalendar
import com.aoztg.greengrim.presentation.customview.YearMonthPickerDialog
import com.aoztg.greengrim.presentation.ui.DataState
import com.aoztg.greengrim.presentation.ui.chat.adapter.CertificationListAdapter
import com.aoztg.greengrim.presentation.ui.chat.chatroom.ChatRoomViewModel
import com.aoztg.greengrim.presentation.ui.chat.chatroom.OnSwipeTouchListener
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@AndroidEntryPoint
class CertificationListBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCertificationListBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val NEXT_PAGE = 0
        const val NEW_DATE = 1
    }

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CertificationListBottomSheetViewModel by viewModels()
    private val chatRoomViewModel: ChatRoomViewModel by activityViewModels()

    private var adapter: CertificationListAdapter? = null
    private var guideJob: Job? = null

    private lateinit var customCalendar: CustomCalendar

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_certification_list_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        setBottomSheetState()
        adapter = CertificationListAdapter()
        viewModel.setChallengeId(chatRoomViewModel.challengeId)
        binding.rvCertifications.adapter = adapter
        setGuideLifeCycle()
        initStateObserve()
        initEventsObserver()
        setScrollEventListener()
        initCustomCalendar()
        setBtnClickListener()
        viewModel.getCertificationList(NEW_DATE)
        viewModel.getCertificationDate()
    }

    private fun setGuideLifeCycle() {
        guideJob = CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            binding.btnCertificationGuide.animate().alpha(0.0f).setDuration(1000)
        }

        binding.btnCertificationGuide.setOnClickListener {
            binding.btnCertificationGuide.visibility = View.GONE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheetState() {
        val behavior = BottomSheetBehavior.from(binding.certificationBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutCertification.visibility = View.INVISIBLE

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.layoutChatBox.visibility = View.VISIBLE
                    binding.layoutCertification.visibility = View.INVISIBLE
                } else {
                    binding.layoutChatBox.visibility = View.INVISIBLE
                    binding.layoutCertification.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.uiState.collect {
                adapter?.submitList(it.certificationList)
            }
        }

        repeatOnStarted {
            viewModel.uiState.collect {
                binding.tvDate.text = it.curDateString
                binding.btnSelectMonth.text = it.curMonthString
            }
        }

        repeatOnStarted {
            viewModel.uiState.collect {
                if (it.dataState == DataState.NO_DATA) {
                    binding.ivNoCertification.visibility = View.VISIBLE
                    binding.tvNoCertification.visibility = View.VISIBLE
                } else {
                    binding.ivNoCertification.visibility = View.INVISIBLE
                    binding.tvNoCertification.visibility = View.INVISIBLE
                }
            }
        }

        repeatOnStarted {
            chatRoomViewModel.uiState.collect{
                if(it.chatInfo.todayCertification){
                    binding.btnCreateCertification.setImageResource(R.drawable.icon_create_certification_off)
                    binding.btnCreateCertification.isClickable = false
                } else {
                    binding.btnCreateCertification.setImageResource(R.drawable.icon_create_certification_on)
                    binding.btnCreateCertification.isClickable = true
                    binding.btnCreateCertification.setOnClickListener {
                        chatRoomViewModel.navigateToCreateCertification()
                    }
                }
            }
        }

        repeatOnStarted {
            chatRoomViewModel.uiState.collect{
                binding.btnSendMessage.isEnabled = it.editTextState
            }
        }

        repeatOnStarted {
            chatRoomViewModel.chatMessage.collect{
                if(it.isBlank()){
                    binding.etChat.setText("")
                }
            }
        }

        binding.etChat.doOnTextChanged { text, _, _, _ ->
            chatRoomViewModel.chatMessage.value = text.toString()
        }

        binding.btnSendMessage.setOnClickListener {
            chatRoomViewModel.sendMessage()
        }
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CertificationListEvents.ShowYearMonthPicker -> {
                        YearMonthPickerDialog(
                            requireContext(),
                            it.curYear,
                            it.curMonth,
                            ::yearMonthDatePickerConfirmListener
                        ).show()
                    }

                    is CertificationListEvents.NavigateToCertificationDetail -> {
                        CertificationListTempDate.setTempDate(customCalendar.selectedDate)
                        findNavController().toCertificationDetail(it.certificationId)
                    }

                    is CertificationListEvents.ShowToastMessage -> {
                        Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                    }

                    is CertificationListEvents.ShowCalendar -> customCalendar.setDateWithDataList(
                        viewModel.uiState.value.certificationDateList
                    )

                    is CertificationListEvents.NavigateToBack -> findNavController().navigateUp()
                    is CertificationListEvents.ShowSnackMessage -> parentViewModel.showSnack(it.msg)
                }
            }
        }
    }

    private fun setScrollEventListener() {

        // NestedScrollView 안에 RecyclerView 삽입하여서, 마지막 아이템 감지로 페이징하면 안됨
        // 또한, 최하단 스크롤 감지를, recyclerview 가 아니라 NestedScrollView 로 해야함


        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                viewModel.getCertificationList(NEXT_PAGE)
            }
        }

//        binding.rvCertifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)
//
//                if (lastVisibleItemPosition == itemTotalCount) {
//                    viewModel.getCertificationList(NEXT_PAGE)
//                }
//            }
//        })
    }

    private fun initCustomCalendar() {
        val tempDate = CertificationListTempDate.getTempDate()
        customCalendar = CustomCalendar(
            binding.calendarView,
            ::monthScrollListener,
            ::dateSelectListener,
            selectedMonth = tempDate.yearMonth,
            selectedDate = tempDate
        )
    }

    private fun setBtnClickListener() {
        binding.btnNextMonth.setOnClickListener {
            customCalendar.goToNextMonth()
        }

        binding.btnPreviousMonth.setOnClickListener {
            customCalendar.goToPreviousMonth()
        }
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

}