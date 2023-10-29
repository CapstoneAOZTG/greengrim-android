package com.aoztg.greengrim.presentation.ui.challenge.category


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeCategoryBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeCategoryAdapter

class ChallengeCategoryFragment :
    BaseFragment<FragmentChallengeCategoryBinding>(R.layout.fragment_challenge_category) {

    private val viewModel: ChallengeCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvChallengeCategory.adapter = ChallengeCategoryAdapter()
        viewModel.getCategoryList()
    }

}