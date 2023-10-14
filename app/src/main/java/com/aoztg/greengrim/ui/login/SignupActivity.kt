package com.aoztg.greengrim.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.databinding.ActivitySignupBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}