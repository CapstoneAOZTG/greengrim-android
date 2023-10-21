package com.aoztg.greengrim.presentation.ui.intro.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentTermsBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.navigateToSignup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>(R.layout.fragment_terms) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setTextListner()
    }

    private fun setListener() {
        with(binding) {
            btnTerms1.setOnCheckedChangeListener { _, state ->
                check()
            }
            btnTerms2.setOnCheckedChangeListener { _, state ->
                check()
            }
            btnTermsAll.setOnClickListener {
                val state = binding.btnTermsAll.isChecked
                allToggle(state)
            }
            btnNext.setOnClickListener {
                findNavController().navigateToSignup()
            }
        }
    }

    private fun setTextListner() {
        with(binding) {
            tvTerms1Head.setOnClickListener {
                this.btnTerms1.isChecked = !this.btnTerms1.isChecked
                check()
            }
            tvTerms2Head.setOnClickListener {
                this.btnTerms2.isChecked = !this.btnTerms2.isChecked
                check()
            }
            tvTermsAllHead.setOnClickListener {
                this.btnTermsAll.isChecked = !this.btnTermsAll.isChecked
                allToggle(this.btnTermsAll.isChecked)
            }
        }
    }

    private fun allToggle(state: Boolean) {
        binding.btnTerms1.isChecked = state
        binding.btnTerms2.isChecked = state
        binding.btnNext.isEnabled = state
    }

    private fun check() {
        if (binding.btnTerms1.isChecked && binding.btnTerms2.isChecked) {
            binding.btnTermsAll.isChecked = true
            binding.btnNext.isEnabled = true
        } else {
            binding.btnTermsAll.isChecked = false
            binding.btnNext.isEnabled = false
        }
    }


}