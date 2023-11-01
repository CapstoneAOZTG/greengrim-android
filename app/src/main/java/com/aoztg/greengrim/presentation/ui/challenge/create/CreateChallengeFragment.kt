package com.aoztg.greengrim.presentation.ui.challenge.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeCategoryAdapter
import com.aoztg.greengrim.presentation.ui.challenge.adapter.OnCategoryItemClickListener
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChallengeFragment :
    BaseFragment<FragmentCreateChallengeBinding>(R.layout.fragment_create_challenge),
    OnCategoryItemClickListener {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CreateChallengeViewModel by viewModels()
    private lateinit var curView: View
    private var isCurViewExists = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvChallengeCategory.adapter = ChallengeCategoryAdapter(this)
        initEventObserver()
        viewModel.getCategoryList()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CreateChallengeEvents.NavigateToCreateDetail -> findNavController().toCreateChallengeDetail(it.category)
                    is CreateChallengeEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    override fun onItemClicked(view: View, category: String) {
        if (isCurViewExists) {
            curView.setBackgroundResource(R.drawable.shape_darkfill_nostroke_radius10)
        }
        curView = view
        isCurViewExists = true
        view.setBackgroundResource(R.drawable.shape_darkfill_whitestroke_radius10)
        viewModel.setSelectedCategory(category)
    }

    private fun NavController.toCreateChallengeDetail(category: String) {
        val action = CreateChallengeFragmentDirections.actionCreateChallengeFragmentToCreateChallengeDetailFragment(category)
        this.navigate(action)
    }
}