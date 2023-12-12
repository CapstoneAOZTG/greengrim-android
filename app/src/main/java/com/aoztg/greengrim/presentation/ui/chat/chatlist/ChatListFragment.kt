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
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatListAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: ChatListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChatList.adapter = ChatListAdapter()
        initEventsObserver()
        initUnReadChatObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChatList()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatListEvents.NavigateToChatRoom -> findNavController().toChatRoom(it.chatName, it.chatId, it.challengeId)
                    is ChatListEvents.CallUnReadChatData -> viewModel.setUnReadChatData(chatManager.unReadChatData)
                    is ChatListEvents.ShowLoading -> showLoading(requireContext())
                    is ChatListEvents.DismissLoading -> dismissLoading()
                    is ChatListEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is ChatListEvents.ShowSnackMessage -> showCustomSnack(binding.rvChatList,it.msg)
                }
            }
        }
    }

    private fun initUnReadChatObserver(){
        repeatOnStarted {
            chatManager.updateUnReadChat.collect{
                viewModel.setUnReadChatData(it)
            }
        }
    }

    private fun NavController.toChatRoom(chatName: String, chatId: Int, challengeId: Int) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(chatId, challengeId, chatName)
        this.navigate(action)
    }
}


