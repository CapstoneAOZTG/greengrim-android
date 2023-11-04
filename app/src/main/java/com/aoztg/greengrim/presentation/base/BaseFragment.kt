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
import com.aoztg.greengrim.presentation.util.LoadingDialog
import com.aoztg.greengrim.presentation.util.OnePopupMenu
import com.aoztg.greengrim.presentation.util.YearMonthPickerDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var onePopupMenu: OnePopupMenu
    private lateinit var yearMonthPickerDialog: YearMonthPickerDialog

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
        loadingDialog = LoadingDialog(context)
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun showOnePopup(
        context: Context,
        onClickListener: () -> Unit,
        xPosition: Int,
        yPosition: Int
    ) {
        onePopupMenu = OnePopupMenu(context, onClickListener)
        onePopupMenu.show(xPosition, yPosition)
    }

    fun dismissOnePopup() {
        onePopupMenu.dismiss()
    }

    fun showYearMonthDialog(
        context: Context,
        curYear: Int,
        curMonth: Int,
        onConfirmBtnClickListener: (year: Int, month: Int) -> Unit
    ){
        yearMonthPickerDialog = YearMonthPickerDialog(context, curYear, curMonth, onConfirmBtnClickListener)
        yearMonthPickerDialog.show()
    }

    fun dismissYearMonthDialog(){
        if(yearMonthPickerDialog.isShowing){
            yearMonthPickerDialog.dismiss()
        }
    }

    fun showCustomToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}