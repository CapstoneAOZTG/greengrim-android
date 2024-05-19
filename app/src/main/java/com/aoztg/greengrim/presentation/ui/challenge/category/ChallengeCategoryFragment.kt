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
import com.aoztg.greengrim.presentation.ui.challenge.model.CategoryName
import com.aoztg.greengrim.presentation.ui.challenge.search.SearchChallengeFragment
import com.aoztg.greengrim.presentation.ui.challenge.search.SearchChallengeFragmentDirections
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChallengeCategoryFragment :
    BaseFragment<FragmentChallengeCategoryBinding>(R.layout.fragment_challenge_category), OnCategoryItemClickListener {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChallengeCategoryViewModel by viewModels()

    private var guideJob : Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChallengeCategory.adapter = ChallengeCategoryAdapter(this)
        initEventObserver()
        setGuideView()
        viewModel.getCategoryList()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeCategoryEvents.NavigateToCreateChallenge -> findNavController().toCreateChallenge()
                    is ChallengeCategoryEvents.NavigateToSearchChallenge -> findNavController().toSearchChallenge()
                }
            }
        }
    }

    private fun setGuideView(){
        guideJob = CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            binding.ivCreateChallengeGuide.animate().alpha(0.0f).setDuration(1000)
        }

        binding.ivCreateChallengeGuide.setOnClickListener {
            binding.ivCreateChallengeGuide.visibility = View.GONE
        }
    }

    override fun onItemClicked(view: View, category: CategoryName) {
        findNavController().toChallengeList(category)
    }

    private fun NavController.toChallengeList(category: CategoryName) {
        val action = ChallengeCategoryFragmentDirections.actionChallengeCategoryFragmentToChallengeListFragment(category.text, category.value)
        this.navigate(action)
    }

    private fun NavController.toCreateChallenge() {
        val action =
            ChallengeCategoryFragmentDirections.actionChallengeCategoryFragmentToCreateChallengeFragment()
        this.navigate(action)
    }

    private fun NavController.toSearchChallenge() {
        val action = ChallengeCategoryFragmentDirections.actionChallengeCategoryFragmentToSearchChallengeFragment()
        navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideJob?.cancel()
    }

}