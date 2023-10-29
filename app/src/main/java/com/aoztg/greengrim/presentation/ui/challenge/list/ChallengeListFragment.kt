package com.aoztg.greengrim.presentation.ui.challenge.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter

class ChallengeListFragment : BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list) {

    private val viewModel: ChallengeListViewModel by viewModels()
    private val args: ChallengeListFragmentArgs by navArgs()
    private val title by lazy{ args.title }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.tvTitle.text = title
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        viewModel.getChallengeRooms()
    }
}