package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatRoomBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListFragmentArgs
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChatRoomViewModel by viewModels()
    private val args: ChatRoomFragmentArgs by navArgs()
    private val chatId by lazy { args.id }

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
                    is ChatRoomEvents.ShowPopupMenu -> {}
                    is ChatRoomEvents.NavigateBack -> findNavController().navigateUp()
                    is ChatRoomEvents.NavigateToCertificationList -> findNavController().toCertificationList(
                        it.id
                    )

                    is ChatRoomEvents.NavigateToMakeCertification -> findNavController().toMakeCertification(
                        it.id
                    )
                }
            }
        }
    }

    private fun NavController.toCertificationList(id: String) {
        val action =
            ChatRoomFragmentDirections.actionChatRoomFragmentToCertificationListFragment(id)
        this.navigate(action)
    }

    private fun NavController.toMakeCertification(id: String) {
        val action =
            ChatRoomFragmentDirections.actionChatRoomFragmentToMakeCertificationFragment(id)
        this.navigate(action)
    }

}