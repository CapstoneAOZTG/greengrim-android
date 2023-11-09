package com.aoztg.greengrim.presentation.ui.global.challengedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeDetailFragment :
    BaseFragment<FragmentChallengeDetailBinding>(R.layout.fragment_challenge_detail) {

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private val id by lazy { args.id }
    private val popupLocation = IntArray(2)
    private var isPopupShowing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setId(id)
        viewModel.getChallengeDetailInfo()
        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeDetailEvents.NavigateBack -> findNavController().navigateUp()
                    is ChallengeDetailEvents.PopUpMenu -> {
                        isPopupShowing = if (isPopupShowing) {
                            dismissOnePopup()
                            false
                        } else{
                            showPopup()
                            true
                        }
                    }

                    is ChallengeDetailEvents.RootClicked -> {
                        if (isPopupShowing) {
                            dismissOnePopup()
                            isPopupShowing = false
                        }
                    }

                    is ChallengeDetailEvents.NavigateChatRoom -> findNavController().toChatRoom(it.id)
                }
            }
        }
    }

    private fun showPopup() {
        val moreBtn = binding.btnMore
        moreBtn.getLocationOnScreen(popupLocation)
        val left = popupLocation[0] + moreBtn.left.toFloat()
        val top = popupLocation[1] + moreBtn.bottom.toFloat()
        showOnePopup(
            requireContext(),
            ::navigateToAccusation,
            left.toInt(),
            top.toInt()
        )
    }

    private fun NavController.toChatRoom(id: String) {
        val action =
            ChallengeDetailFragmentDirections.actionChallengeDetailFragmentToChatRoomFragment(id)
        this.navigate(action)
    }

    private fun navigateToAccusation() {
        dismissOnePopup()
        isPopupShowing = false
        showCustomToast("신고하기로 이동 구현 전")
    }
}