package com.aoztg.greengrim.presentation.ui.chat.chatroom

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
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
import com.aoztg.greengrim.presentation.customview.TodayCertificationDialog
import com.aoztg.greengrim.presentation.ui.chat.adapter.ChatMessageAdapter
import com.aoztg.greengrim.presentation.ui.chat.certificationlist.CertificationListBottomSheetFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.toProfile
import com.aoztg.greengrim.presentation.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: ChatRoomViewModel by activityViewModels()

    private var certificationBottomSheetFragment: CertificationListBottomSheetFragment? = null

    private val args: ChatRoomFragmentArgs by navArgs()
    private val chatId by lazy { args.chatId }
    private val challengeId by lazy { args.challengeId }
    private val chatName by lazy { args.chatName }
    private val popupLocation = IntArray(2)
    private val adapter = ChatMessageAdapter()
    private var guideJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatManager.inChat(chatId)
        initBottomSheet()
        binding.vm = viewModel
        binding.tvHeader.text = chatName
        parentViewModel.hideBNV()
        binding.rvChat.adapter = adapter
        binding.rvChat.itemAnimator = null
        setScrollEventListener()
        viewModel.setIds(chatId, challengeId)
        viewModel.getChatInfo()
        setDataChangeListener()
        initEventsObserver()
        initChatMessageObserver()
        initStateObserve()
    }

    private fun initBottomSheet() {
        if (childFragmentManager.findFragmentById(R.id.certification_bottom_sheet) == null) {
            certificationBottomSheetFragment = CertificationListBottomSheetFragment()
            childFragmentManager.beginTransaction().add(
                R.id.certification_bottom_sheet,
                certificationBottomSheetFragment!!
            ).commit()
        }
    }

    private fun setScrollEventListener() {

        binding.rvChat.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                viewModel.setScrollState(!recyclerView.canScrollVertically(1))

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
                    is ChatRoomEvents.ShowTodayCertification -> showTodayCertificationDialog()
                    is ChatRoomEvents.NavigateBack -> findNavController().navigateUp()
                    is ChatRoomEvents.NavigateToCertificationList -> navigateToCertificationList()
                    is ChatRoomEvents.NavigateToCreateCertification -> findNavController().toCreateCertification()
                    is ChatRoomEvents.NavigateToCertificationDetail -> findNavController().toCertificationDetail(
                        it.id
                    )

                    is ChatRoomEvents.NavigateToProfile -> findNavController().toProfile(it.id)

                    is ChatRoomEvents.SendMessage -> chatManager.sendMessage(
                        it.chatId,
                        it.message,
                        it.isChild
                    )

                    is ChatRoomEvents.ScrollBottom -> {
                        scrollRecyclerViewBottom()
                    }
                    is ChatRoomEvents.ExitChat -> {
                        chatManager.exitChat(chatId)
                        dismissLoading()
                        showCustomToast("채팅방 나가기 성공")
                        findNavController().navigateUp()
                    }

                    is ChatRoomEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is ChatRoomEvents.ShowSnackMessage -> parentViewModel.showSnack(it.msg)
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

    private fun initStateObserve(){
        repeatOnStarted {
            viewModel.uiState.collect{
                adapter.submitList(it.chatMessages.toMutableList())
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
        showChatPopUp(
            requireContext(),
            ::navigateToChallengeInfo,
            ::navigateToCertificationList,
            ::exitChat,
            left.toInt(),
            top.toInt()
        )
    }

    private fun showTodayCertificationDialog() {
        TodayCertificationDialog(
            requireContext()
        ) {
            findNavController().toCreateCertification()
        }.show()
    }

    private fun navigateToChallengeInfo() {
        val action = MainNavDirections.actionGlobalToChallengeDetailFragment(viewModel.challengeId)
        findNavController().navigate(action)
    }

    private fun navigateToCertificationList() {
    }

    private fun exitChat() {
        viewModel.exitChallenge()
    }

    private fun NavController.toCreateCertification() {
        val action = ChatRoomFragmentDirections.actionChatRoomFragmentToCreateCertificationFragment(
            challengeId,
            chatId,
            viewModel.uiState.value.chatInfo.certificationCount,
            chatName,
            viewModel.uiState.value.chatInfo.category,
            viewModel.uiState.value.chatInfo.participantCount,
        )
        this.navigate(action)
    }

    override fun onStop() {
        super.onStop()
        chatManager.outChat()
        chatManager.storeRecentReadTime(chatId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
        dismissChatPopUp()
        guideJob?.cancel()
    }

}

