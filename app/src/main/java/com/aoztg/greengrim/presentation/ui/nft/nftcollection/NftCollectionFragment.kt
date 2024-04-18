package com.aoztg.greengrim.presentation.ui.nft.nftcollection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftCollectionBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.nft.adapter.NftCollectionAdapter
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftCollectionFragment :
    BaseFragment<FragmentNftCollectionBinding>(R.layout.fragment_nft_collection) {

    private val viewModel: NftCollectionViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var sortType = NftSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvNftCollectionList.adapter = NftCollectionAdapter()
        initEventObserver()
        setScrollEventListener()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is NftListEvents.NavigateToNftDetail -> findNavController().toNftDetail(it.id)
                    is NftListEvents.ShowLoading -> showLoading(requireContext())
                    is NftListEvents.DismissLoading -> dismissLoading()
                    is NftListEvents.ShowSnackMessage -> showCustomSnack(
                        binding.layoutFilter,
                        it.msg
                    )

                    is NftListEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvNftCollectionList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getNftCollectionList(viewModel.uiState.value.curFilter.text)
                }
            }
        })
    }
}