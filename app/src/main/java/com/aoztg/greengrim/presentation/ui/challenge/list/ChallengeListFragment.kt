package com.aoztg.greengrim.presentation.ui.challenge.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.ChallengeFilterBottomSheet
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel.Companion.ORIGINAL
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChallengeListFragment :
    BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list) {

    private val viewModel: ChallengeListViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: ChallengeListFragmentArgs by navArgs()
    private val categoryText by lazy { args.categoryText }
    private val categoryValue by lazy { args.categoryValue }
    private var sortType = ChallengeSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.tvTitle.text = categoryText
        viewModel.setCategory(categoryValue)
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        initEventObserver()
        setScrollEventListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChallengeList(ORIGINAL)
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeListEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )

                    is ChallengeListEvents.NavigateToCreateChallenge -> findNavController().toCreateChallenge()
                    is ChallengeListEvents.ShowBottomSheet -> showBottomSheet()
                    is ChallengeListEvents.ScrollToTop -> binding.rvChallengeList.smoothScrollToPosition(0)
                    is ChallengeListEvents.ShowLoading -> showLoading(requireContext())
                    is ChallengeListEvents.DismissLoading -> dismissLoading()
                    is ChallengeListEvents.ShowSnackMessage -> showCustomSnack(binding.rvChallengeList, it.msg)
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvChallengeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getChallengeList(ORIGINAL)
                }
            }
        })
    }

    private fun showBottomSheet() {
        ChallengeFilterBottomSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }

    private fun NavController.toCreateChallenge() {
        val action =
            ChallengeListFragmentDirections.actionChallengeListFragmentToCreateChallengeFragment()
        this.navigate(action)
    }
}

