package com.aoztg.greengrim.ui.intro.terms

import android.os.Bundle
import androidx.activity.viewModels
import com.aoztg.greengrim.databinding.ActivityTermsBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.intro.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsActivity : BaseActivity<ActivityTermsBinding>(ActivityTermsBinding::inflate) {

    private val viewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}