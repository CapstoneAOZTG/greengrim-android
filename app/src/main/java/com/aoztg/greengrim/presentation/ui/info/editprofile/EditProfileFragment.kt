package com.aoztg.greengrim.presentation.ui.info.editprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentEditProfileBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.pvm = parentViewModel
        initStateObserver()
        initEventObserver()
        initImageObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.completeBtnState) {
                    is BaseState.Success -> binding.btnComplete.isEnabled = true
                    is BaseState.Failure -> binding.btnComplete.isEnabled = false
                    else -> {}
                }

                when (it.nickState) {
                    is BaseState.Success -> binding.tvWarning.visibility = View.INVISIBLE
                    is BaseState.Error -> {
                        binding.tvWarning.visibility = View.VISIBLE
                        binding.tvWarning.text = it.nickState.msg
                    }

                    else -> {}
                }

                when (it.editProfileState) {
                    is BaseState.Success -> findNavController().navigateUp()
                    is BaseState.Error -> showCustomToast(it.editProfileState.msg)
                    else -> {}
                }

                when (it.getProfileState) {
                    is ProfileState.Success -> setProfileImg(it.getProfileState.imgUrl)
                    is ProfileState.Error -> showCustomToast(it.getProfileState.msg)
                    else -> {}
                }
            }
        }
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is EditProfileEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initImageObserver() {
        repeatOnStarted {
            parentViewModel.image.collect {
                if (it.isNotBlank()) {
                    viewModel.setProfileImg(it)
                }
            }
        }
    }

    private fun setProfileImg(url: String) {
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.icon_profile)
            .into(binding.ivProfile)
    }

}