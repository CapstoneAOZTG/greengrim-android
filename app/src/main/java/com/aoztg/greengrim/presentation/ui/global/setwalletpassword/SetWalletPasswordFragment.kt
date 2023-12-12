package com.aoztg.greengrim.presentation.ui.global.setwalletpassword

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentSetWalletPasswordBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetWalletPasswordFragment :
    BaseFragment<FragmentSetWalletPasswordBinding>(R.layout.fragment_set_wallet_password) {

    private val viewModel: SetWalletPasswordViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    private lateinit var numButtons: List<AppCompatButton>
    private lateinit var passwordViews: List<ImageView>

    companion object {
        const val ERASE = 0
        const val TYPE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
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
                    is SetWalletPasswordEvents.TypePassword -> {
                        setPasswordViews(TYPE, it.idx)
                    }

                    is SetWalletPasswordEvents.ErasePassword -> {
                        setPasswordViews(ERASE, it.idx)
                    }

                    is SetWalletPasswordEvents.ChangeModeToCheck -> {
                        binding.tvPasswordHeader.text = "지갑 비밀번호 확인"
                        binding.tvPasswordAnnounce.text = "비밀번호를 다시 한번 입력해주세요"
                        passwordViews.forEach { iv ->
                            iv.setImageResource(R.drawable.icon_password_unfill)
                        }
                    }

                    is SetWalletPasswordEvents.RemovePasswordViews -> {
                        passwordViews.forEach { iv ->
                            iv.setImageResource(R.drawable.icon_password_unfill)
                        }
                    }

                    is SetWalletPasswordEvents.ShowSnackMessage -> showCustomSnack(
                        binding.tvPasswordAnnounce,
                        it.msg
                    )

                    is SetWalletPasswordEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is SetWalletPasswordEvents.NavigateToBack -> findNavController().navigateUp()
                    is SetWalletPasswordEvents.ShowLoading -> showLoading(requireContext())
                    is SetWalletPasswordEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun setPasswordViews(type: Int, idx: Int) {
        when (type) {
            TYPE -> {
                passwordViews[idx].setImageResource(R.drawable.icon_password_fill)
            }

            ERASE -> {
                passwordViews[idx].setImageResource(R.drawable.icon_password_unfill)
            }
        }
    }
}
