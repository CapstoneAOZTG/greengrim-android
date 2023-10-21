package com.aoztg.greengrim.presentation.ui.intro.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSignupBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.viewmodel.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup) {

    private val parentViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        initObserver()
    }

    private fun initObserver() {
        repeatOnStarted {
            parentViewModel.uiState.collect {
                when (it.signupState) {
                    true -> binding.btnNext.isEnabled = true
                    else -> binding.btnNext.isEnabled = false
                }

                when (it.nickState) {
                    true -> binding.tvWarning.visibility = View.INVISIBLE
                    else -> binding.tvWarning.visibility = View.VISIBLE
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


