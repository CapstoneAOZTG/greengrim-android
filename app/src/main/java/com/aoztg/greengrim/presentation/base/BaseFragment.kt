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
import com.aoztg.greengrim.presentation.customview.CustomSnackBar
import com.aoztg.greengrim.presentation.customview.FourPopupMenu
import com.aoztg.greengrim.presentation.customview.LoadingDialog
import com.aoztg.greengrim.presentation.customview.OnePopupMenu
import com.aoztg.greengrim.presentation.customview.VerifySnackBar
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
    private var onePopupMenu: OnePopupMenu? = null
    private var fourPopupMenu: FourPopupMenu? = null
    private lateinit var yearMonthPickerDialog: YearMonthPickerDialog
    private var loadingState = false

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

    fun showOnePopup(
        context: Context,
        onClickListener: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        onePopupMenu = OnePopupMenu(context, onClickListener)
        onePopupMenu?.show(xPosition, yPosition)
    }

    fun dismissOnePopup() {
        onePopupMenu?.dismiss()
    }

    fun showFourPopup(
        context: Context,
        onClickChallengeInfo: () -> Unit,
        onClickCertificationList: () -> Unit,
        onClickAccusation: () -> Unit,
        onClickExit: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        fourPopupMenu = FourPopupMenu(
            context,
            onClickChallengeInfo,
            onClickCertificationList,
            onClickAccusation,
            onClickExit
        )
        fourPopupMenu?.show(xPosition, yPosition)
    }

    fun dismissFourPopup() {
        fourPopupMenu?.dismiss()
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