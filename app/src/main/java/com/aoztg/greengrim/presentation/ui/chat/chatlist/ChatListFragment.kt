package com.aoztg.greengrim.presentation.ui.chat.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.chatmanager.ChatViewModel
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatListAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()
    private val viewModel: ChatListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChatList.adapter = ChatListAdapter()
        viewModel.getChatList()
        initEventsObserver()
        initUnReadChatObserver()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatListEvents.NavigateToChatRoom -> findNavController().toChatRoom(it.chatId, it.challengeId)
                    is ChatListEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is ChatListEvents.CallUnReadChatData -> viewModel.setUnReadChatData(chatViewModel.unReadChatData)
                }
            }
        }
    }

    private fun initUnReadChatObserver(){
        repeatOnStarted {
            chatViewModel.updateUnReadChat.collect{
                viewModel.setUnReadChatData(it)
            }
        }
    }

    private fun NavController.toChatRoom(chatId: Int, challengeId: Int) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(chatId, challengeId)
        this.navigate(action)
    }
}


