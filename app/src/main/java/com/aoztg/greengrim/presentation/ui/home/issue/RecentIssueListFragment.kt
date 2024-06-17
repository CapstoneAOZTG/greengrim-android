package com.aoztg.greengrim.presentation.ui.home.issue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentRecentIssueListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.home.adapter.RecentIssueListAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecentIssueListFragment: BaseFragment<FragmentRecentIssueListBinding>(R.layout.fragment_recent_issue_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: RecentIssueListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        initEventObserve()
        binding.rvIssueList.adapter = RecentIssueListAdapter()
        viewModel.getIssueList()
        recyclerListener()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is RecentIssueEvent.NavigateToIssueDetail -> findNavController().toRecentIssueDetail(it.id)
                    is RecentIssueEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun recyclerListener(){
        binding.rvIssueList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getIssueList()
                }
            }
        })
    }

    private fun NavController.toRecentIssueDetail(id: Long){
        val action = RecentIssueListFragmentDirections.actionRecentIssueListFragmentToRecentIssueDetailFragment(id)
        navigate(action)
    }

}