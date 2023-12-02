package com.aoztg.greengrim.presentation.ui.challenge.create

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.customview.CustomSnackBar
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CreateChallengeDetailFragment :
    BaseFragment<FragmentCreateChallengeDetailBinding>(R.layout.fragment_create_challenge_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: CreateChallengeDetailViewModel by viewModels()

    private val args: CreateChallengeDetailFragmentArgs by navArgs()
    private val categoryText by lazy { args.categoryText }
    private val categoryValue by lazy {args.categoryValue }

    private lateinit var curView: TextView
    private var isCurViewExist = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        binding.vm = viewModel
        binding.tvHeader.text = "$categoryText 챌린지 생성"
        viewModel.setCategory(categoryValue)
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            parentViewModel.image.collect {
                viewModel.setImageUrl(it)
            }
        }

        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.randomKeywordState) {
                    is KeywordState.Set -> {
                        viewModel.setKeyword("")
                        delay(100)
                        setChipListener()
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
                    is CreateChallengeDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is CreateChallengeDetailEvents.NavigateToChatList -> {
                        chatManager.subscribeNewChat(it.chatId)
                        findNavController().toChatList()
                    }
                    is CreateChallengeDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CreateChallengeDetailEvents.ShowSnackMessage -> showCustomSnack(binding.etTitle, it.msg)
                    is CreateChallengeDetailEvents.ShowLoading -> showLoading(requireContext())
                    is CreateChallengeDetailEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun setChipListener() {
        binding.chipgroupKeywords.children.forEach { view ->
            view.setOnClickListener {
                if (isCurViewExist) {
                    curView.setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                }
                isCurViewExist = true
                curView = view as TextView
                curView.setBackgroundResource(R.drawable.shape_greenfill_nostroke_radius20)
                viewModel.setKeyword(curView.text.toString())
            }
        }
    }

    private fun NavController.toChatList(){
        val action = CreateChallengeDetailFragmentDirections.actionCreateChallengeDetailFragmentToChatListFragment()
        this.navigate(action)
    }

}

