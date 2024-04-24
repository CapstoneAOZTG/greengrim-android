package com.aoztg.greengrim.presentation.ui.challenge.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateChallengeDetailFragment :
    BaseFragment<FragmentCreateChallengeDetailBinding>(R.layout.fragment_create_challenge_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: CreateChallengeDetailViewModel by viewModels()

    private val args: CreateChallengeDetailFragmentArgs by navArgs()
    private val categoryText by lazy { args.categoryText }
    private val categoryValue by lazy { args.categoryValue }

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
            parentViewModel.imageUri.collect {
                binding.ivAddPhoto.setImageURI(it)
            }
        }

        repeatOnStarted {
            parentViewModel.imageFile.collect {
                viewModel.setImageFile(it)
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CreateChallengeDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is CreateChallengeDetailEvents.NavigateToChatList -> {
                        chatManager.subscribeNewChat(
                            it.chatId,
                            it.challengeId,
                            it.title,
                            it.titleImg
                        )
                        findNavController().toChatList()
                    }

                    is CreateChallengeDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CreateChallengeDetailEvents.ShowSnackMessage -> showCustomSnack(
                        binding.etTitle,
                        it.msg
                    )

                    is CreateChallengeDetailEvents.ShowLoading -> showLoading(requireContext())
                    is CreateChallengeDetailEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun NavController.toChatList() {
        val action =
            CreateChallengeDetailFragmentDirections.actionCreateChallengeDetailFragmentToChatListFragment()
        this.navigate(action)
    }

}

