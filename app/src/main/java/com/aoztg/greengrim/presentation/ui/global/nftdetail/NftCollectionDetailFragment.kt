package com.aoztg.greengrim.presentation.ui.global.nftdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftCollectionDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftCollectionDetailFragment :
    BaseFragment<FragmentNftCollectionDetailBinding>(R.layout.fragment_nft_collection_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: NftCollectionDetailViewModel by viewModels()
    private val args: NftCollectionDetailFragmentArgs by navArgs()
    private val id by lazy { args.id }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getNftCollectionDetail(id)
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is NftCollectionDetailEvent.NavigateToBack -> findNavController().navigateUp()
                    is NftCollectionDetailEvent.ShowSnackMessage -> parentViewModel.showSnack(it.msg)
                    is NftCollectionDetailEvent.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }
}