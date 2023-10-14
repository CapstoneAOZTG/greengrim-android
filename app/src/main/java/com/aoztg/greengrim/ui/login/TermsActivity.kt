package com.aoztg.greengrim.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.databinding.ActivityTermsBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsActivity : BaseActivity<ActivityTermsBinding>(ActivityTermsBinding::inflate) {

    private val viewModel : ViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}