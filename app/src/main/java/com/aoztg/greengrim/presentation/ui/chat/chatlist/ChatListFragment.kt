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
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatRoomInterface
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list),
    ChatRoomInterface {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: ChatListViewModel by viewModels()
    private val adapter = ChatListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.chatMg = chatManager
        binding.vm = viewModel
        adapter.setChatRoomInterface(this)
        binding.rvChatList.adapter = adapter
        binding.rvChatList.itemAnimator = null
        initEventsObserve()
    }

    private fun initEventsObserve() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatListEvents.CallUnReadChatData -> {}
                    is ChatListEvents.ShowLoading -> showLoading(requireContext())
                    is ChatListEvents.DismissLoading -> dismissLoading()
                    is ChatListEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is ChatListEvents.ShowSnackMessage -> showCustomSnack(
                        binding.rvChatList,
                        it.msg
                    )
                }
            }
        }
    }

    override fun navigateToChatRoom(chatName: String, chatId: Long, challengeId: Long) {
        findNavController().toChatRoom(chatName, chatId, challengeId)
    }

    private fun NavController.toChatRoom(chatName: String, chatId: Long, challengeId: Long) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(
            chatId,
            challengeId,
            chatName
        )
        this.navigate(action)
    }

}


