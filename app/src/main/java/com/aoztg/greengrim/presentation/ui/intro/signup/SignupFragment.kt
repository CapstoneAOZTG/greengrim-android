package com.aoztg.greengrim.presentation.ui.intro.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSignupBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.IntroViewModel
import com.bumptech.glide.Glide
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
                    is SignupState.Success -> binding.btnNext.isEnabled = true
                    is SignupState.Failure -> {
                        binding.btnNext.isEnabled = false
                    }
                    else -> {}
                }

                when (it.nickState) {
                    is SignupState.Success -> binding.tvWarning.visibility = View.INVISIBLE
                    is SignupState.Error -> {
                        binding.tvWarning.visibility = View.VISIBLE
                        binding.tvWarning.text = it.nickState.msg
                    }
                    else -> {}
                }

                when (it.signupState) {
                    is SignupState.Success -> parentViewModel.goToMain()
                    is SignupState.Error -> showCustomToast(it.signupState.msg)
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

@BindingAdapter("profileImgUrl")
fun bindProfileImg(imageView: ImageView, url: String) {
    if(url.isNotBlank()){
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.icon_profile)
            .into(imageView)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(view: TextView, text: String, limit: Int) {
    view.text = "(${text.length}/$limit)"
}


