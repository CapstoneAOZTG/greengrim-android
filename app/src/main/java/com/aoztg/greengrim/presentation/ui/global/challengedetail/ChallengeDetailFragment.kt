package com.aoztg.greengrim.presentation.ui.global.challengedetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.global.model.ChallengeDetail
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeDetailFragment :
    BaseFragment<FragmentChallengeDetailBinding>(R.layout.fragment_challenge_detail) {

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }
    private val popupLocation = IntArray(2)
    private var isPopupShowing = false
    private var loadingState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setId(challengeId)
        viewModel.getChallengeDetailInfo()
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect{
                when(it.getChallengeDetailState){
                    is BaseState.Error -> showCustomToast(it.getChallengeDetailState.msg)
                    else -> {}
                }

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
                    is ChallengeDetailEvents.PopUpMenu -> {
                        isPopupShowing = if (isPopupShowing) {
                            dismissOnePopup()
                            false
                        } else{
                            showPopup()
                            true
                        }
                    }

                    is ChallengeDetailEvents.RootClicked -> {
                        if (isPopupShowing) {
                            dismissOnePopup()
                            isPopupShowing = false
                        }
                    }

                    is ChallengeDetailEvents.NavigateChatRoom -> {
                        parentViewModel.connectNewChat(it.id)
                        findNavController().toChatRoom(it.id)
                    }
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

    private fun NavController.toChatRoom(id: Int) {
        val action =
            ChallengeDetailFragmentDirections.actionChallengeDetailFragmentToChatRoomFragment(id)
        this.navigate(action)
    }

    private fun navigateToAccusation() {
        dismissOnePopup()
        isPopupShowing = false
        showCustomToast("신고하기로 이동 구현 전")
    }
}

@BindingAdapter("challengeDetailBtnText")
fun bindChallengeDetailBtnText(button: Button, data: ChallengeDetail){

    if( data.entered ){
        button.text = "이미 참여중인 챌린지 입니다"
        button.isEnabled = false
        button.setTextColor(Color.WHITE)
    } else {
        button.text = "입장하기"
        button.isEnabled = true
        button.setTextColor(Color.BLACK)
    }
}