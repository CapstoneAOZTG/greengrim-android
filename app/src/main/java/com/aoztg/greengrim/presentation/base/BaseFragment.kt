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
import com.aoztg.greengrim.presentation.customview.BlockAccusationPopUpMenu
import com.aoztg.greengrim.presentation.customview.ChatPopUpMenu
import com.aoztg.greengrim.presentation.customview.CustomSnackBar
import com.aoztg.greengrim.presentation.customview.EditDeletePopUpMenu
import com.aoztg.greengrim.presentation.customview.LoadingDialog
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
    private var chatPopUpMenu: ChatPopUpMenu? = null
    private var blockAccusationPopUpMenu: BlockAccusationPopUpMenu? = null
    private var editDeletePopUpMenu: EditDeletePopUpMenu? = null
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

    fun showBlockAccusationPopUp(
        context: Context,
        onClickBlock: () -> Unit,
        onClickAccusation: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        blockAccusationPopUpMenu =
            BlockAccusationPopUpMenu(context, onClickBlock, onClickAccusation)
        blockAccusationPopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissBlockAccusationPopUp() {
        blockAccusationPopUpMenu?.dismiss()
    }

    fun showEditDeletePopUp(
        context: Context,
        onClickEdit: () -> Unit,
        onClickDelete: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        editDeletePopUpMenu = EditDeletePopUpMenu(context, onClickEdit, onClickDelete)
        editDeletePopUpMenu?.show(xPosition, yPosition)
    }

    fun dismissEditDeletePopUp() {
        editDeletePopUpMenu?.dismiss()
    }

    fun showChatPopUp(
        context: Context,
        onClickChallengeInfo: () -> Unit,
        onClickCertificationList: () -> Unit,
        onClickBlock: () -> Unit,
        onClickAccusation: () -> Unit,
        onClickExit: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        chatPopUpMenu = ChatPopUpMenu(
            context,
            onClickChallengeInfo,
            onClickCertificationList,
            onClickBlock,
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