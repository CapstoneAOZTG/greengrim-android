package com.aoztg.greengrim.presentation.ui.challenge.category


import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeCategoryBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeCategoryAdapter
import com.aoztg.greengrim.presentation.ui.challenge.adapter.OnCategoryItemClickListener
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class ChallengeCategoryFragment :
    BaseFragment<FragmentChallengeCategoryBinding>(R.layout.fragment_challenge_category), OnCategoryItemClickListener {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChallengeCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChallengeCategory.adapter = ChallengeCategoryAdapter(this)
        initEventObserver()
        viewModel.getCategoryList()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeCategoryEvents.NavigateToCreateChallenge -> findNavController().toCreateChallenge()
                }
            }
        }
    }

    override fun onItemClicked(view: View, category: String) {
        findNavController().toChallengeList(category)
    }

    private fun NavController.toChallengeList(category: String) {
        val action =
            ChallengeCategoryFragmentDirections.actionChallengeCategoryFragmentToChallengeListFragment(
                category
            )
        this.navigate(action)
    }

    private fun NavController.toCreateChallenge() {
        val action =
            ChallengeCategoryFragmentDirections.actionChallengeCategoryFragmentToCreateChallengeFragment()
        this.navigate(action)
    }

}