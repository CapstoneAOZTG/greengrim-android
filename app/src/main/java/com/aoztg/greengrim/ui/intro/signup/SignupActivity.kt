package com.aoztg.greengrim.ui.intro.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.aoztg.greengrim.databinding.ActivitySignupBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.intro.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}