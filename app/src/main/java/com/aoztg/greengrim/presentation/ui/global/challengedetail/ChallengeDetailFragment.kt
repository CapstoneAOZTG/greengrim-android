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
import com.aoztg.greengrim.presentation.chatmanager.ChatViewModel
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeDetailFragment :
    BaseFragment<FragmentChallengeDetailBinding>(R.layout.fragment_challenge_detail) {

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()

    private val args: ChallengeDetailFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }
    private val popupLocation = IntArray(2)
    private var loadingState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setChallengeId(challengeId)
        viewModel.getChallengeDetailInfo()
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect{
                when(it.loading){
                    is LoadingState.IsLoading -> {
                        if(it.loading.state){
                            if(!loadingState){
                                showLoading(requireContext())
                                loadingState = true
                            }
                        } else{
                            dismissLoading()
                            loadingState = false
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChallengeDetailEvents.NavigateBack -> findNavController().navigateUp()
                    is ChallengeDetailEvents.PopUpMenu -> showPopup()
                    is ChallengeDetailEvents.NavigateChatRoom -> {
                        chatViewModel.subscribeNewChat(it.chatId)
                        findNavController().toChatRoom(it.chatId, it.challengeId)
                    }
                    is ChallengeDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
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

    private fun NavController.toChatRoom(chatId: Int, challengeId: Int) {
        val action = ChallengeDetailFragmentDirections.actionChallengeDetailFragmentToChatRoomFragment(chatId, challengeId)
        this.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissOnePopup()
    }

    private fun navigateToAccusation() {
        showCustomToast("신고하기로 이동 구현 전")
    }
}

