package com.aoztg.greengrim.presentation.ui.global.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentWebviewBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class WebViewFragment: BaseFragment<FragmentWebviewBinding>(R.layout.fragment_webview) {

    private val args : WebViewFragmentArgs by navArgs()
    private val url by lazy{args.url}
    private val parentViewModel : MainViewModel by activityViewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.webview.loadUrl(url)
        with(binding.webview){
            settings.javaScriptEnabled = true
        }
    }
}