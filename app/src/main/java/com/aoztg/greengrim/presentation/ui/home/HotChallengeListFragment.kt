package com.aoztg.greengrim.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHotChallengeListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeCategory
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotChallengeListFragment :
    BaseFragment<FragmentHotChallengeListBinding>(R.layout.fragment_hot_challenge_list) {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: HotChallengeListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel

        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        setScrollEventListener()
        viewModel.getHotChallengeList(ChallengeListViewModel.ORIGINAL)
        initEventObserve()
    }

    private fun setScrollEventListener() {

        binding.rvChallengeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getHotChallengeList(NEXT_PAGE)
                }
            }
        })
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is HotChallengeListEvents.ScrollToTop -> binding.rvChallengeList.smoothScrollToPosition(
                        0
                    )

                    is HotChallengeListEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )

                    is HotChallengeListEvents.NavigateToChallengeCategory -> findNavController().toChallengeCategory()
                    is HotChallengeListEvents.ShowLoading -> showLoading(requireContext())
                    is HotChallengeListEvents.DismissLoading -> dismissLoading()
                    is HotChallengeListEvents.ShowSnackMessage -> showCustomSnack(
                        binding.cgFilter,
                        it.msg
                    )
                    is HotChallengeListEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

}