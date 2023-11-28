package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aoztg.greengrim.MainNavDirections
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChatRoomBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatMessageAdapter
import com.aoztg.greengrim.presentation.ui.main.KeyboardState
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChatRoomViewModel by viewModels()
    private val args: ChatRoomFragmentArgs by navArgs()
    private val chatId by lazy { args.chatId }
    private val challengeId by lazy { args.challengeId }
    private val popupLocation = IntArray(2)
    private var isPopupShowing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
        binding.rvChat.adapter = ChatMessageAdapter()
        viewModel.setIds(chatId, challengeId)
        initEventsObserver()
        initChatMessageObserver()
        initKeyboardObserver()
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

                    is ChatRoomEvents.SendMessage -> parentViewModel.sendMessage(
                        it.chatId,
                        it.message
                    )

                    is ChatRoomEvents.ScrollBottom -> scrollRecyclerViewBottom()
                    else -> {}
                }
            }
        }
    }

    private fun initChatMessageObserver() {
        repeatOnStarted {
            parentViewModel.newChat.collect {
                if (it.roomId == chatId) {
                    viewModel.newChatMessage(it)
                    scrollRecyclerViewBottom()
                }
            }
        }
    }

    private fun initKeyboardObserver() {
        repeatOnStarted {
            parentViewModel.keyboardState.collectLatest {
                if (it == KeyboardState.SHOW) {
                    scrollRecyclerViewBottom()
                }
            }
        }
    }

    private fun scrollRecyclerViewBottom() {
        val lastVisibleItemPosition =
            (binding.rvChat.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        val itemTotalCount = binding.rvChat.adapter?.itemCount?.minus(1)
        itemTotalCount?.let {
            if (lastVisibleItemPosition != it) {
                binding.rvChat.smoothScrollToPosition(it+ 2)
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
        val action =
            ChatRoomFragmentDirections.actionChatRoomFragmentToCertificationListFragment(viewModel.chatRoomId)
        findNavController().navigate(action)
    }

    private fun navigateToAccusation() {
        showCustomToast("신고하기로 이동 구현전")
    }

    private fun exitChat() {
        showCustomToast("나가기 구현 전")
        findNavController().navigateUp()
    }

    private fun NavController.toCreateCertification() {
        val action = ChatRoomFragmentDirections.actionChatRoomFragmentToCreateCertificationFragment(
            challengeId,
            chatId
        )
        this.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissFourPopup()
    }
}

@BindingAdapter("chatImgUrl")
fun bindChatImg(imageView: ImageView, url: String?) {
    if (url.isNullOrBlank()) {
        imageView.visibility = View.GONE
    } else {
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.test)
            .into(imageView)
    }
}