package com.aoztg.greengrim.presentation.ui.challenge.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter


class ChallengeListFragment :
    BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list) {

    private val viewModel: ChallengeListViewModel by viewModels()
    private val args: ChallengeListFragmentArgs by navArgs()
    private val category by lazy { args.category }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.tvTitle.text = category
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        viewModel.getChallengeRooms()
        initObserver()
    }

    private fun initObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.loading) {
                    is LoadingState.IsLoading -> {
                        if (it.loading.state) showLoading(requireContext())
                        else dismissLoading()
                    }

                    else -> {}
                }
            }
        }

        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is ChallengeListEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(it.id)
                }
            }
        }
    }

    private fun NavController.toChallengeDetail(id: String) {
        val action = ChallengeListFragmentDirections.actionChallengeListFragmentToChallengeDetailFragment(id)
        this.navigate(action)
    }
}