package com.aoztg.greengrim.presentation.ui.challenge.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment

class CreateChallengeDetailFragment : BaseFragment<FragmentCreateChallengeDetailBinding>(R.layout.fragment_create_challenge_detail) {

    private val viewModel : CreateChallengeDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}