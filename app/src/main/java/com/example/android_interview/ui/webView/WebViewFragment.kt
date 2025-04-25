package com.example.android_interview.ui.webView

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.example.android_interview.base.BaseFragment
import com.example.android_interview.databinding.FragmentWebViewBinding
import com.example.android_interview.extension.viewBinding
import com.example.android_interview.model.loading.Loading
import kotlinx.coroutines.launch

enum class WebViewFragmentArgs(val key: String) {
    URL("url"),
}

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    override val binding: FragmentWebViewBinding by viewBinding()

    override fun FragmentWebViewBinding.initView() {
        onBinding()
    }

    private fun onBinding() {
        val url = arguments?.getString(WebViewFragmentArgs.URL.key) ?: ""
        binding.apply {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    lifecycleScope.launch {
                        Loading.hide()
                    }
                }
            }
            webView.loadUrl(url)
        }
    }
}