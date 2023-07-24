package com.chaquopy.example

import android.annotation.SuppressLint
import android.util.Log
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import com.chaquo.python.PyObject


class MainPresenter(val activity: MainActivity) {

    lateinit var webView: WebView;

    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView(): WebView {

        val webView = activity.findViewById<WebView>(R.id.mainWebView)
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.clearCache(true)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//        webView.addJavascriptInterface(AndroidAPI(this), "Android")
        WebView.setWebContentsDebuggingEnabled(true)


        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("WebView:Console", consoleMessage.message())
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {

            var browserView: PyObject? = null // Will be set from python

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                Log.w("DM", "request ${request.url}")
                return super.shouldInterceptRequest(view, request)
            }


            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                return false;
            }

            override fun onPageFinished(w: WebView, url: String) {
                super.onPageFinished(w, url)
                browserView?.callAttr("on_load_finished")
                Log.w("DM", "finish load $url")
            }
        }

        this.webView = webView
        return webView;
    }

    fun toggleFullscreen(){
        if ((activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0){
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun toast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}