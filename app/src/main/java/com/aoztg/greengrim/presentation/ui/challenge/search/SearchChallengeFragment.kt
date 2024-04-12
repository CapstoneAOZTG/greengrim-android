package com.aoztg.greengrim.presentation.ui.challenge.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSearchChallengeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchChallengeFragment :
    BaseFragment<FragmentSearchChallengeBinding>(R.layout.fragment_search_challenge) {

    private val args: SearchChallengeFragmentArgs by navArgs()
    private val categoryValue by lazy { args.category }

    private val viewModel: SearchChallengeViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setCategoryValue(categoryValue)
        binding.rvSearchResult.adapter = ChallengeRoomAdapter()
        setScrollEventListener()
        initEventObserve()
    }

    private fun setScrollEventListener() {

        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getChallengeList()
                }
            }
        })
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SearchChallengeEvent.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )

                    is SearchChallengeEvent.ShowSnackMessage -> showCustomSnack(
                        binding.etSearch,
                        it.msg
                    )

                    is SearchChallengeEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }
}