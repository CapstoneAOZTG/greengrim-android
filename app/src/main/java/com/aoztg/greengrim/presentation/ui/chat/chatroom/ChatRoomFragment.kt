package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.MainNavDirections
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatRoomBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatMessageAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: ChatRoomViewModel by viewModels()

    private val args: ChatRoomFragmentArgs by navArgs()
    private val chatId by lazy { args.chatId }
    private val challengeId by lazy { args.challengeId }
    private val popupLocation = IntArray(2)
    private val adapter = ChatMessageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readChat()
        binding.vm = viewModel
        parentViewModel.hideBNV()
        binding.rvChat.adapter = adapter
        binding.rvChat.itemAnimator = null
        setScrollEventListener()
        viewModel.setIds(chatId, challengeId)
        setDataChangeListener()
        initEventsObserver()
        initChatMessageObserver()
    }

    private fun setScrollEventListener() {

        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = binding.rvChat.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getChatMessageData()
                }
            }
        })
    }

    private fun setDataChangeListener() {
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChatRoomEvents.ShowPopupMenu -> showPopup()
                    is ChatRoomEvents.NavigateBack -> findNavController().navigateUp()
                    is ChatRoomEvents.NavigateToCertificationList -> navigateToCertificationList()
                    is ChatRoomEvents.NavigateToCreateCertification -> findNavController().toCreateCertification()
                    is ChatRoomEvents.NavigateToCertificationDetail -> findNavController().toCertificationDetail(
                        it.id
                    )

                    is ChatRoomEvents.SendMessage -> chatManager.sendMessage(
                        it.chatId,
                        it.message,
                    )

                    is ChatRoomEvents.ScrollBottom -> scrollRecyclerViewBottom()
                    is ChatRoomEvents.ExitChat -> {
                        chatManager.exitChat(chatId)
                        dismissLoading()
                        showCustomToast("채팅방 나가기 성공")
                        findNavController().navigateUp()
                    }

                    is ChatRoomEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is ChatRoomEvents.ShowSnackMessage -> showCustomSnack(binding.tvTodayCertification,it.msg)
                    is ChatRoomEvents.ShowLoading -> showLoading(requireContext())
                    is ChatRoomEvents.DismissLoading -> showLoading(requireContext())
                }
            }
        }
    }

    private fun initChatMessageObserver() {
        repeatOnStarted {
            chatManager.newChat.collect {
                if (it.roomId == chatId) {
                    viewModel.newChatMessage(it)
                }
            }
        }
    }

    private fun scrollRecyclerViewBottom() {
        binding.rvChat.scrollToPosition(0)
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
        val action =
            ChatRoomFragmentDirections.actionChatRoomFragmentToCertificationListFragment(viewModel.chatRoomId)
        findNavController().navigate(action)
    }

    private fun navigateToAccusation() {
        showCustomToast("신고하기로 이동 구현전")
    }

    private fun exitChat() {
        viewModel.exitChallenge()
    }

    private fun NavController.toCreateCertification() {
        val action = ChatRoomFragmentDirections.actionChatRoomFragmentToCreateCertificationFragment(
            challengeId,
            chatId
        )
        this.navigate(action)
    }

    private fun readChat() {
        chatManager.readChat(chatId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissFourPopup()
        readChat()
    }
}

