package com.aoztg.greengrim.presentation.ui.intro.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSignupBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseUiState
import com.aoztg.greengrim.presentation.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pvm = parentViewModel
        binding.vm = viewModel
        initStateObserver()
        initEventObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.nextBtnState) {
                    is BaseUiState.Success -> binding.btnNext.isEnabled = true
                    is BaseUiState.Failure -> {
                        binding.btnNext.isEnabled = false
                    }

                    else -> {}
                }

                when (it.nickState) {
                    is BaseUiState.Success -> binding.tvWarning.visibility = View.INVISIBLE
                    is BaseUiState.Error -> {
                        binding.tvWarning.visibility = View.VISIBLE
                        binding.tvWarning.text = it.nickState.msg
                    }

                    else -> {}
                }

                when (it.signupState) {
                    is BaseUiState.Success -> parentViewModel.goToMain()
                    is BaseUiState.Error -> showCustomSnack(binding.tvTitle, it.signupState.msg)
                    else -> {}
                }
            }
        }

        repeatOnStarted {
            parentViewModel.imageUri.collect {
                binding.ivProfile.setImageURI(it)
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
                    is SignupEvents.ShowLoading -> showLoading(requireContext())
                    is SignupEvents.DismissLoading -> dismissLoading()
                    is SignupEvents.ShowSnackMessage -> showCustomSnack(binding.ivProfile, it.msg)
                    is SignupEvents.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }
}




