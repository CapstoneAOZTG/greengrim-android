package com.aoztg.greengrim.presentation.ui.info.mypaint

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyPaintBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.getGrimNftSortSheet
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.market.GrimNftSortType
import com.aoztg.greengrim.presentation.ui.market.MarketViewModel
import com.aoztg.greengrim.presentation.ui.market.adapter.GrimItemAdapter
import com.aoztg.greengrim.presentation.ui.toGrimDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPaintFragment : BaseFragment<FragmentMyPaintBinding>(R.layout.fragment_my_paint) {

    private val viewModel: MyPaintViewModel by viewModels()
    private var sortType = GrimNftSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvPaintList.adapter = GrimItemAdapter()
        initEventObserver()
        setScrollEventListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGrimList(ChallengeListViewModel.ORIGINAL)
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyPaintEvents.NavigateToGrimDetail -> findNavController().toGrimDetail(it.id)
                    is MyPaintEvents.ShowBottomSheet -> showBottomSheet()
                    is MyPaintEvents.ScrollToTop -> binding.rvPaintList.smoothScrollToPosition(0)
                    is MyPaintEvents.ShowLoading -> showLoading(requireContext())
                    is MyPaintEvents.DismissLoading -> dismissLoading()
                    is MyPaintEvents.ShowSnackMessage -> showCustomSnack(
                        binding.rvPaintList,
                        it.msg
                    )

                    is MyPaintEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvPaintList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getGrimList(MarketViewModel.ORIGINAL)
                }
            }
        })
    }

    private fun showBottomSheet() {
        getGrimNftSortSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }
}