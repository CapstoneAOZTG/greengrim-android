package com.aoztg.greengrim.ui.intro.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.aoztg.greengrim.databinding.ActivitySignupBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.intro.action.SignupNavigationAction
import com.aoztg.greengrim.ui.intro.viewmodel.SignupViewModel
import com.aoztg.greengrim.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    private val viewModel : SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        setNavigationObserver()
        setTextWatcher()
    }

    private fun setNavigationObserver(){
        lifecycleScope.launchWhenStarted {
            viewModel.navigationHandler.collectLatest {
                when(it){
                    is SignupNavigationAction.NavigateToMainActivity -> {
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    }

                    is SignupNavigationAction.NavigateToGallery -> {

                    }
                }
            }
        }
    }

    private fun setTextWatcher(){
        with(binding){
            etNickname.addTextChangedListener(getTextWatcher(8, this.tvNicknameCount, true))
            etIntroduce.addTextChangedListener(getTextWatcher(200,this.tvIntroduceCount, false ))

        }

    }

    private fun getTextWatcher(
        limit : Int,
        countView : TextView,
        isNick : Boolean
    ) : TextWatcher{

        val textWatcher = object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let{
                    countView.text = "($it/$limit)"

                    if(isNick){
                        viewModel.checkNick(it.toString())
                    }

                }?:run{
                    countView.text = "(0/$limit)"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        }

        return textWatcher
    }




}