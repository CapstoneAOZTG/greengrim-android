package com.aoztg.greengrim.presentation.ui.challenge.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

class ChallengeDetailFragment : BaseFragment<FragmentChallengeDetailBinding>(R.layout.fragment_challenge_detail) {

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private val id by lazy{args.id}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getChallengeDetailInfo()
    }
}