package com.aoztg.greengrim.presentation.ui.market.nftlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.GrimNftFilterBottomSheet
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.market.GrimNftSortType
import com.aoztg.greengrim.presentation.ui.market.MarketViewModel
import com.aoztg.greengrim.presentation.ui.market.adapter.GrimItemAdapter
import com.aoztg.greengrim.presentation.ui.market.adapter.NftItemAdapter
import com.aoztg.greengrim.presentation.ui.market.nftlist.NftListViewModel.Companion.ORIGINAL
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftListFragment : BaseFragment<FragmentNftListBinding>(R.layout.fragment_nft_list) {

    private val viewModel: NftListViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var sortType = GrimNftSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvNftList.adapter = NftItemAdapter()
        initEventObserver()
        setScrollEventListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGrimList(ORIGINAL)
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is NftListEvents.NavigateToNftDetail -> findNavController().toNftDetail(it.id)
                    is NftListEvents.ShowBottomSheet -> showBottomSheet()
                    is NftListEvents.ScrollToTop -> binding.rvNftList.smoothScrollToPosition(0)
                    is NftListEvents.ShowLoading -> showLoading(requireContext())
                    is NftListEvents.DismissLoading -> dismissLoading()
                    is NftListEvents.ShowSnackMessage -> showCustomSnack(
                        binding.rvNftList,
                        it.msg
                    )

                    is NftListEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvNftList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
        GrimNftFilterBottomSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }
}