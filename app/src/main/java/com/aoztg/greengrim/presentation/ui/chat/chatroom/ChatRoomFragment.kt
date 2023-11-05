package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.MainNavDirections
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatRoomBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChatRoomViewModel by viewModels()
    private val args: ChatRoomFragmentArgs by navArgs()
    private val chatId by lazy { args.id }
    private val popupLocation = IntArray(2)
    private var isPopupShowing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
        viewModel.setChatId(chatId)
        initEventsObserver()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatRoomEvents.ShowPopupMenu -> {
                        isPopupShowing = if (isPopupShowing) {
                            dismissFourPopup()
                            false
                        } else{
                            showPopup()
                            true
                        }
                    }
                    is ChatRoomEvents.DismissPopupMenu -> {
                        if(isPopupShowing){
                            dismissFourPopup()
                            isPopupShowing = false
                        }
                    }
                    is ChatRoomEvents.NavigateBack -> findNavController().navigateUp()
                    is ChatRoomEvents.NavigateToCertificationList -> navigateToCertificationList()
                    is ChatRoomEvents.NavigateToMakeCertification -> findNavController().toMakeCertification(it.id)
                }
            }
        }
    }

    private fun showPopup() {
        val moreBtn = binding.btnMore
        moreBtn.getLocationOnScreen(popupLocation)
        val left = popupLocation[0] + moreBtn.left.toFloat()
        val top = popupLocation[1] + moreBtn.bottom.toFloat()
        showFourPopup(
            requireContext(),
            ::navigateToChallengeInfo,
            ::navigateToCertificationList,
            ::navigateToAccusation,
            ::exitChat,
            left.toInt(),
            top.toInt()
        )
    }

    private fun navigateToChallengeInfo() {
        val action = MainNavDirections.actionGlobalToChallengeDetailFragment(viewModel.challengeId)
        findNavController().navigate(action)
    }

    private fun navigateToCertificationList() {
        val action = ChatRoomFragmentDirections.actionChatRoomFragmentToCertificationListFragment(viewModel.chatRoomId)
        findNavController().navigate(action)
    }

    private fun navigateToAccusation() {
        showCustomToast("신고하기로 이동 구현전")
    }

    private fun exitChat() {
        showCustomToast("나가기 구현 전")
        findNavController().navigateUp()
    }

    private fun NavController.toMakeCertification(id: String) {
        val action = ChatRoomFragmentDirections.actionChatRoomFragmentToMakeCertificationFragment(id)
        this.navigate(action)
    }

}