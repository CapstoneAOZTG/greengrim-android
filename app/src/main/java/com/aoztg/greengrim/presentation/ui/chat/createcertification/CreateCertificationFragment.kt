package com.aoztg.greengrim.presentation.ui.chat.createcertification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCertificationFragment :
    BaseFragment<FragmentCreateCertificationBinding>(R.layout.fragment_create_certification) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val chatManager: ChatManager by activityViewModels()
    private val viewModel: CreateCertificationViewModel by viewModels()

    private val args: CreateCertificationFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }
    private val chatId by lazy { args.chatId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setIds(challengeId)
        binding.pvm = parentViewModel
        binding.vm = viewModel
        viewModel.getCertificationDefaultData()
        initImgObserver()
        initEventObserver()
    }

    private fun initImgObserver() {
        repeatOnStarted {
            parentViewModel.imageUri.collect {
                binding.ivAddPhoto.setImageURI(it)
            }
        }

        repeatOnStarted {
            parentViewModel.imageFile.collect{
                viewModel.setImageFile(it)
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CreateCertificationEvents.NavigateToBack -> findNavController().navigateUp()
                    is CreateCertificationEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CreateCertificationEvents.SendCertificationMessage -> chatManager.sendCertificationMessage(
                        chatId = chatId,
                        message = it.message,
                        certId = it.certId,
                        certImg = it.certImg
                    )
                    is CreateCertificationEvents.ShowSnackMessage -> showCustomSnack(binding.tvTitle,it.msg)
                    is CreateCertificationEvents.ShowLoading -> showLoading(requireContext())
                    is CreateCertificationEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }
}

