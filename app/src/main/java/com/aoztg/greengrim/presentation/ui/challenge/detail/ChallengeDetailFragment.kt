package com.aoztg.greengrim.presentation.ui.challenge.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class ChallengeDetailFragment :
    BaseFragment<FragmentChallengeDetailBinding>(R.layout.fragment_challenge_detail) {

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private val id by lazy { args.id }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.getChallengeDetailInfo()
        initObserver()
    }

    private fun initObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeDetailEvents.NavigateBack -> findNavController().navigateUp()
                    is ChallengeDetailEvents.PopUpMenu -> {}
                    is ChallengeDetailEvents.NavigateChatRoom -> {}
                }
            }
        }
    }
}