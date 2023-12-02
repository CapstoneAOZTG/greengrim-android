package com.aoztg.greengrim.presentation.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aoztg.greengrim.presentation.customview.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<B : ViewDataBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {
    protected lateinit var binding: B
    private lateinit var loadingDialog : LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }


    fun showLoading(context : Context){
        loadingDialog = LoadingDialog(context)
        loadingDialog.show()
    }

    fun dismissLoading(){
        if(loadingDialog.isShowing){
            loadingDialog.dismiss()
        }
    }

    fun showCustomToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showSnackBar(text: String, action: String? = null) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            action?.let {
                setAction(it) {}
            }
            show()
        }
    }

}