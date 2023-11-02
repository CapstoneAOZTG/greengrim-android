package com.aoztg.greengrim.presentation.ui.intro.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSignupBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
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
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.nextBtnState) {
                    is BaseState.Success -> binding.btnNext.isEnabled = true
                    is BaseState.Failure -> {
                        binding.btnNext.isEnabled = false
                    }

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

                when (it.signupState) {
                    is BaseState.Success -> parentViewModel.goToMain()
                    is BaseState.Error -> showCustomToast(it.signupState.msg)
                    else -> {}
                }
            }
        }

        repeatOnStarted {
            parentViewModel.profileImg.collect {
                if (it.isNotBlank()) {
                    viewModel.setProfileImg(it)
                }
            }
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(view: TextView, text: String, limit: Int) {
    view.text = "(${text.length}/$limit)"
}


