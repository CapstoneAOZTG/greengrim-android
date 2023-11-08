package com.aoztg.greengrim.presentation.ui.challenge.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.util.getSortSheet


class ChallengeListFragment :
    BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list) {

    private val viewModel: ChallengeListViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: ChallengeListFragmentArgs by navArgs()
    private val categoryText by lazy { args.categoryText }
    private val categoryValue by lazy { args.categoryValue }
    private var sortType = ChallengeSortType.RECENT
    private var loadingState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.tvTitle.text = categoryText
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
                        if (it.loading.state) {
                            showLoading(requireContext())
                            loadingState = true
                        } else {
                            dismissLoading()
                            loadingState = false
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
                    is ChallengeListEvents.NavigateToCreateChallenge -> findNavController().toCreateChallenge()
                    is ChallengeListEvents.ShowBottomSheet -> showBottomSheet()
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

    private fun NavController.toCreateChallenge() {
        val action = ChallengeListFragmentDirections.actionChallengeListFragmentToCreateChallengeFragment()
        this.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingState) dismissLoading()
    }
}