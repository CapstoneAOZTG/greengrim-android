package com.aoztg.greengrim.presentation.ui.chat.chatlist

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatListAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChatListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChatList.adapter = ChatListAdapter()
        viewModel.getChatList()
        initEventsObserver()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatListEvents.NavigateToChatRoom -> findNavController().toChatRoom(it.id)
                }
            }
        }
    }

    private fun NavController.toChatRoom(id: Int) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(id)
        this.navigate(action)
    }
}

@BindingAdapter("chatCreationText")
fun bindChatListDday(textView: TextView, dDay: String) {
    if (dDay == "오늘") {
        textView.setBackgroundResource(R.drawable.shape_greenfill_nostroke_radius20)
    } else {
        textView.setBackgroundResource(R.drawable.shape_redfill_nostroke_radius20)
    }
    textView.text = dDay
}
