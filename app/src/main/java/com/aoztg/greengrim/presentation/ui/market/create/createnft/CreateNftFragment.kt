package com.aoztg.greengrim.presentation.ui.market.create.createnft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.databinding.FragmentCreateNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.checkpassword.FormBeforePasswordInput
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCheckPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNftFragment : BaseFragment<FragmentCreateNftBinding>(R.layout.fragment_create_nft) {

    private val viewModel: CreateNftViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: CreateNftFragmentArgs by navArgs()
    private val grimId by lazy { args.grimId }
    private val grimUrl by lazy { args.grimUrl }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setImgUrl(grimUrl)
        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CreateNftEvents.CreateNft -> {
                        navigateToCheckPassword(it.title, it.description)
                    }

                    is CreateNftEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun navigateToCheckPassword(
        title: String,
        description: String
    ) {
        FormBeforePasswordInput.createNft(
            CreateNftRequest(
                password = "",
                grimId = grimId,
                title = title,
                description = description,
            )
        )
        findNavController().toCheckPassword()
    }

}