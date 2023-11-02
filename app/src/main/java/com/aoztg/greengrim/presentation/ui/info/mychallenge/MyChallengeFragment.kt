package com.aoztg.greengrim.presentation.ui.info.mychallenge

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyChallengeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListEvents
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.util.getSortSheet

class MyChallengeFragment: BaseFragment<FragmentMyChallengeBinding>(R.layout.fragment_my_challenge) {

    private val viewModel: ChallengeListViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var sortType = ChallengeSortType.RECENT
    private var loadingState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        viewModel.getChallengeRooms()
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.loading) {
                    is LoadingState.IsLoading -> {
                        loadingState = if (it.loading.state) {
                            showLoading(requireContext())
                            true
                        } else {
                            dismissLoading()
                            false
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeListEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(it.id)
                    is ChallengeListEvents.ShowBottomSheet -> showBottomSheet()
                    else -> {}
                }
            }
        }
    }

    private fun showBottomSheet() {
        getSortSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingState) dismissLoading()
    }
}