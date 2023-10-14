package com.aoztg.greengrim.ui.intro.terms

import android.os.Bundle
import androidx.activity.viewModels
import com.aoztg.greengrim.databinding.ActivityTermsBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsActivity : BaseActivity<ActivityTermsBinding>(ActivityTermsBinding::inflate) {

    private val viewModel : TermsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        setObserver()
    }

    private fun setObserver(){
        viewModel.isFirstClicked.observe(this){
            if(it){
                
            }
        }

        viewModel.isSecondClicked.observe(this){

        }

        viewModel.isAllClicked.observe(this){

        }
    }
}