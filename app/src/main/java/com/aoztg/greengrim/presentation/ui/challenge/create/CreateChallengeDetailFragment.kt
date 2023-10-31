package com.aoztg.greengrim.presentation.ui.challenge.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class CreateChallengeDetailFragment :
    BaseFragment<FragmentCreateChallengeDetailBinding>(R.layout.fragment_create_challenge_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CreateChallengeDetailViewModel by viewModels()
    private val args: CreateChallengeDetailFragmentArgs by navArgs()
    private val category by lazy { args.category }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        binding.vm = viewModel
        binding.tvHeader.text = "$category 챌린지 생성"
        imageObserver()
    }

    private fun imageObserver() {
        repeatOnStarted {
            parentViewModel.image.collect {
                viewModel.setImageUrl(it)
            }
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("progressText")
fun bindProgressText(view: TextView, progressState: ProgressState) {
    when (progressState) {
        is ProgressState.Changed -> view.text = progressState.text
        else -> {}
    }
}