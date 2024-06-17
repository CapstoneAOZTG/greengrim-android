package com.aoztg.greengrim.presentation.ui.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAnnounceDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnounceDetailFragment :
    BaseFragment<FragmentAnnounceDetailBinding>(R.layout.fragment_announce_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: AnnounceDetailViewModel by viewModels()
    private val args: AnnounceDetailFragmentArgs by navArgs()
    private val id by lazy { args.id }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.getAnnounceDetail(id)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}