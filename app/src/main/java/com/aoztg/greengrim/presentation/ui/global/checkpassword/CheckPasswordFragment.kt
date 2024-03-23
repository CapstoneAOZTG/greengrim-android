package com.aoztg.greengrim.presentation.ui.global.checkpassword

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCheckPasswordBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.setwalletpassword.SetWalletPasswordEvents
import com.aoztg.greengrim.presentation.ui.global.setwalletpassword.SetWalletPasswordFragment
import com.aoztg.greengrim.presentation.ui.toMarket
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckPasswordFragment: BaseFragment<FragmentCheckPasswordBinding>(R.layout.fragment_check_password) {

    private val viewModel: CheckPasswordViewModel by viewModels()

    private lateinit var numButtons: List<AppCompatButton>
    private lateinit var passwordViews: List<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setViewList()
        setBtnListener()
        initEventObserver()
    }

    private fun setViewList() {
        with(binding) {
            numButtons = listOf(
                btnZero,
                btnOne,
                btnTwo,
                btnThree,
                btnFour,
                btnFive,
                btnSix,
                btnSeven,
                btnEight,
                btnNine
            )

            passwordViews = listOf(
                ivPw1, ivPw2, ivPw3, ivPw4, ivPw5, ivPw6
            )
        }
    }

    private fun setBtnListener() {
        numButtons.forEachIndexed { i, btn ->
            btn.setOnClickListener {
                viewModel.typePassword(i.toString())
            }
        }

        binding.btnErase.setOnClickListener {
            viewModel.erasePassword()
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CheckPasswordEvents.TypePassword -> {
                        setPasswordViews(SetWalletPasswordFragment.TYPE, it.idx)
                    }

                    is CheckPasswordEvents.ErasePassword -> {
                        setPasswordViews(SetWalletPasswordFragment.ERASE, it.idx)
                    }

                    is CheckPasswordEvents.RemovePasswordViews -> {
                        passwordViews.forEach { iv ->
                            iv.setImageResource(R.drawable.icon_password_unfill)
                        }
                    }

                    is CheckPasswordEvents.ShowSnackMessage -> showCustomSnack(
                        binding.tvPasswordAnnounce,
                        it.msg
                    )

                    is CheckPasswordEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CheckPasswordEvents.NavigateToBack -> findNavController().navigateUp()
                    is CheckPasswordEvents.ShowLoading -> showLoading(requireContext())
                    is CheckPasswordEvents.DismissLoading -> dismissLoading()
                    is CheckPasswordEvents.NavigateToMarket -> {
                        findNavController().popBackStack(R.id.nft_fragment,true)
                        findNavController().toMarket()
                    }
                }
            }
        }
    }

    private fun setPasswordViews(type: Int, idx: Int) {
        when (type) {
            SetWalletPasswordFragment.TYPE -> {
                passwordViews[idx].setImageResource(R.drawable.icon_password_fill)
            }

            SetWalletPasswordFragment.ERASE -> {
                passwordViews[idx].setImageResource(R.drawable.icon_password_unfill)
            }
        }
    }
}