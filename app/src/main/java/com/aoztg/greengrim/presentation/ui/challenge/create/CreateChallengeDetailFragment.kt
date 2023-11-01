package com.aoztg.greengrim.presentation.ui.challenge.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.delay

class CreateChallengeDetailFragment :
    BaseFragment<FragmentCreateChallengeDetailBinding>(R.layout.fragment_create_challenge_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CreateChallengeDetailViewModel by viewModels()
    private val args: CreateChallengeDetailFragmentArgs by navArgs()
    private val category by lazy { args.category }

    private lateinit var curView: TextView
    private var isCurViewExist = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        binding.vm = viewModel
        binding.tvHeader.text = "$category 챌린지 생성"
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            parentViewModel.image.collect {
                viewModel.setImageUrl(it)
            }
        }

        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.randomKeywordState) {
                    is KeywordState.Set -> {
                        viewModel.setKeyword("")
                        delay(100)
                        setChipListener()
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
                    is CreateChallengeDetailEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setChipListener() {
        binding.chipgroupKeywords.children.forEach { view ->
            view.setOnClickListener {
                if (isCurViewExist) {
                    curView.setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                }
                isCurViewExist = true
                curView = view as TextView
                curView.setBackgroundResource(R.drawable.shape_greenfill_nostroke_radius20)
                viewModel.setKeyword(curView.text.toString())
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

@BindingAdapter("selectChipList")
fun bindSelectChips(chipGroup: ChipGroup, keywordState: KeywordState) {
    when (keywordState) {
        is KeywordState.Set -> {
            chipGroup.removeAllViews()
            keywordState.keywords.forEach { data ->
                val chip = TextView(chipGroup.context).apply {
                    text = data
                    setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                    setTextAppearance(R.style.TextGgSmallBlack)
                    setPadding(20, 4, 20, 4)
                }

                chipGroup.addView(chip)
            }
        }

        else -> {}
    }
}