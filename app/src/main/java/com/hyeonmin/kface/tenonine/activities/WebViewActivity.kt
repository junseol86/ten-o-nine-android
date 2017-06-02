package com.hyeonmin.kface.tenonine.activities

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.webkit.WebView

import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.utils.Secrets
import com.hyeonmin.kface.tenonine.utils.Singleton
import android.webkit.WebViewClient
import java.lang.Exception
import java.lang.System.exit


class WebViewActivity : Activity() {

    var webView : WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webview) as WebView

        webView?.setBackgroundColor(Color.TRANSPARENT)
        webView?.setInitialScale(1)
        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.loadWithOverviewMode = true
        webView?.settings?.useWideViewPort = true
        webView?.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView?.isScrollbarFadingEnabled = false
        webView?.settings?.setSupportZoom(true)
        webView?.settings?.builtInZoomControls = false

        webView?.loadUrl("${Secrets.webUrl}/${Singleton.tenONineAccessToken.replace('/', '@')}")
        webView?.setWebViewClient(DontOpenBrowserWebview())
    }

    private inner class DontOpenBrowserWebview : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onBackPressed() {
        if (webView != null) {
            if (webView!!.canGoBack()) {
                webView!!.goBack()
            } else {
                super.onBackPressed()
                exit(0)
            }
        } else {
            super.onBackPressed()
            exit(0)
        }
    }
}
