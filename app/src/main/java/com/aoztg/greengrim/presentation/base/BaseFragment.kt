package com.aoztg.greengrim.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aoztg.greengrim.presentation.customview.AccusationContentType
import com.aoztg.greengrim.presentation.customview.AccusationDialog
import com.aoztg.greengrim.presentation.customview.ChallengeNftPopUpMenu
import com.aoztg.greengrim.presentation.customview.ChatPopUpMenu
import com.aoztg.greengrim.presentation.customview.CustomSnackBar
import com.aoztg.greengrim.presentation.customview.LoadingDialog
import com.aoztg.greengrim.presentation.customview.MyChallengeNftPopUpMenu
import com.aoztg.greengrim.presentation.customview.ProfilePopUpMenu
import com.aoztg.greengrim.presentation.customview.TwoButtonTitleDialog
import com.aoztg.greengrim.presentation.customview.YearMonthPickerDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private var profilePopUpMenu: ProfilePopUpMenu? = null
    private var chatPopUpMenu: ChatPopUpMenu? = null
    private var challengeNftPopUpMenu: ChallengeNftPopUpMenu? = null
    private var myChallengeNftPopUpMenu: MyChallengeNftPopUpMenu? = null
    private lateinit var yearMonthPickerDialog: YearMonthPickerDialog
    private var loadingState = false
    private var twoButtonTitleDialog: TwoButtonTitleDialog? = null
    private var accusationDialog: AccusationDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun showLoading(context: Context) {
        if (!loadingState) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            loadingState = true
        }
    }

    fun dismissLoading() {
        if (loadingState) {
            loadingDialog.dismiss()
            loadingState = false
        }
    }

    fun showAccusation(
        context: Context,
        title: String,
        accusationClickListener: (AccusationContentType, String) -> Unit
    ) {
        accusationDialog = AccusationDialog(context, title, accusationClickListener)
        accusationDialog?.show()
    }

    fun dismissAccusation() {
        accusationDialog?.dismiss()
    }

    fun showChallengeNftPopUp(
        context: Context,
        onClickBlock: () -> Unit,
        onClickAccusation: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        challengeNftPopUpMenu = ChallengeNftPopUpMenu(context, onClickBlock, onClickAccusation)
        challengeNftPopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissChallengeNftPopUp() {
        challengeNftPopUpMenu?.dismiss()
    }

    fun showMyChallengeNftPopUp(
        context: Context,
        onClickEdit: () -> Unit,
        onClickDelete: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        myChallengeNftPopUpMenu = MyChallengeNftPopUpMenu(context, onClickEdit, onClickDelete)
        myChallengeNftPopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissMyChallengeNftPopUp() {
        myChallengeNftPopUpMenu?.dismiss()
    }

    fun showProfilePopUp(
        context: Context,
        onClickListener: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        profilePopUpMenu = ProfilePopUpMenu(context, onClickListener)
        profilePopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissProfilePopUp() {
        profilePopUpMenu?.dismiss()
    }

    fun showChatPopUp(
        context: Context,
        onClickChallengeInfo: () -> Unit,
        onClickCertificationList: () -> Unit,
        onClickAccusation: () -> Unit,
        onClickExit: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        chatPopUpMenu = ChatPopUpMenu(
            context,
            onClickChallengeInfo,
            onClickCertificationList,
            onClickAccusation,
            onClickExit
        )
        chatPopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissChatPopUp() {
        chatPopUpMenu?.dismiss()
    }

    fun showYearMonthDialog(
        context: Context,
        curYear: Int,
        curMonth: Int,
        onConfirmBtnClickListener: (year: Int, month: Int) -> Unit
    ) {
        yearMonthPickerDialog =
            YearMonthPickerDialog(context, curYear, curMonth, onConfirmBtnClickListener)
        yearMonthPickerDialog.show()
    }

    fun dismissYearMonthDialog() {
        if (yearMonthPickerDialog.isShowing) {
            yearMonthPickerDialog.dismiss()
        }
    }

    fun showTwoButtonTitleDialog(
        context: Context,
        title: String,
        oneBtnText: String,
        twoBtnText: String,
        confirmListener: () -> Unit
    ) {
        twoButtonTitleDialog =
            TwoButtonTitleDialog(context, title, oneBtnText, twoBtnText, confirmListener)
        twoButtonTitleDialog?.show()
    }

    fun dismissTwoButtonTitleDialog() {
        twoButtonTitleDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    fun showCustomToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showSnackBar(text: String, action: String? = null) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_SHORT
        ).apply {
            action?.let {
                setAction(it) {}
            }
            show()
        }
    }

    fun showCustomSnack(
        view: View,
        text: String,
    ) {
        CustomSnackBar.make(view, text).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingState) {
            loadingDialog.dismiss()
        }
        _binding = null
    }

}