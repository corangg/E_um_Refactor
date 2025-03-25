package com.app.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityAddressBinding
import com.core.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : BaseActivity<ActivityAddressBinding>(ActivityAddressBinding::inflate) {

    override fun setUi() {
        setupWebView()
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(BridgeInterface(), getString(R.string.js_name))

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webView.loadUrl(getString(R.string.js_url))
            }
        }

        webView.loadUrl(getString(R.string.web_view_url))
    }

    inner class BridgeInterface {
        @JavascriptInterface
        fun processDATA(data: String) {
            val intent = Intent().apply {
                val addressData = data.split(",")
                putExtra("address", addressData[0])
                putExtra("zoneCode", addressData.getOrNull(1) ?: "")
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}