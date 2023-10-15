package com.aoztg.greengrim.ui.intro.terms

import android.os.Bundle
import com.aoztg.greengrim.databinding.ActivityTermsBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsActivity : BaseActivity<ActivityTermsBinding>(ActivityTermsBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListener()
    }

    private fun setListener(){
        with(binding){
            btnTerms1.setOnCheckedChangeListener { _, state ->
                check()
            }
            btnTerms2.setOnCheckedChangeListener { _, state ->
                check()
            }
            btnTermsAll.setOnClickListener {
                val state = binding.btnTermsAll.isChecked
                binding.btnTerms1.isChecked = state
                binding.btnTerms2.isChecked = state
                binding.btnNext.isEnabled = state
            }

        }
    }

    private fun check(){
        if( binding.btnTerms1.isChecked && binding.btnTerms2.isChecked){
            binding.btnTermsAll.isChecked = true
            binding.btnNext.isEnabled = true
        } else {
            binding.btnTermsAll.isChecked = false
            binding.btnNext.isEnabled = false
        }
    }


}