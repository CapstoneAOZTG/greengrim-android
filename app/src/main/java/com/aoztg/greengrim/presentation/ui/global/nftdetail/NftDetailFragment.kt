package com.aoztg.greengrim.presentation.ui.global.nftdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftDetailFragment : BaseFragment<FragmentNftDetailBinding>(R.layout.fragment_nft_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: NftDetailViewModel by viewModels()
    private val args: NftDetailFragmentArgs by navArgs()
    private val nftId by lazy { args.nftId }
    private val popupLocation = IntArray(2)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setNftId(nftId)
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is NftDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is NftDetailEvents.ShowSnackMessage -> parentViewModel.showSnack(it.msg)
                    is NftDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is NftDetailEvents.ShowPopUp -> showPopup()
                    is NftDetailEvents.NavigateToProfile -> findNavController().toProfile(it.id)
                }
            }
        }
    }

    private fun showPopup() {
        val moreBtn = binding.btnMore
        moreBtn.getLocationOnScreen(popupLocation)
        val left = popupLocation[0] + moreBtn.left.toFloat()
        val top = popupLocation[1] + moreBtn.bottom.toFloat()

        if (viewModel.uiState.value.nftDetail.mine) {
            showEditDeletePopUp(
                requireContext(),
                { viewModel.editNft() },
                { viewModel.deleteNft() },
                left.toInt(),
                top.toInt()
            )
        } else {
            showBlockAccusationPopUp(
                requireContext(),
                { viewModel.blockNft() },
                ::showAccusation,
                left.toInt(),
                top.toInt()
            )
        }
    }

    private fun showAccusation() {
        showAccusation(requireContext(), "NFT 신고") { accType, content ->
            viewModel.accusationNft(accType, content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissEditDeletePopUp()
        dismissBlockAccusationPopUp()
    }


}