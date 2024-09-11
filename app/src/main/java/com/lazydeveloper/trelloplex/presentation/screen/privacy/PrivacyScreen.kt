package com.lazydeveloper.trelloplex.presentation.screen.privacy

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lazydeveloper.trelloplex.R
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

@SuppressLint("SetJavaScriptEnabled")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun PrivacyScreen() {
    val context = LocalContext.current
    val adServers = StringBuilder()
    var line: String? = ""
    val inputStream = context.resources.openRawResource(R.raw.adblockserverlist)
    val br = BufferedReader(InputStreamReader(inputStream))
    val frameVideo = remember { mutableStateOf("<html><body><iframe src=\"https://www.2embed.cc/embed/tt10676048\" width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\" allowfullscreen></iframe></body></html>") }

    try {
        while (br.readLine().also { line = it } != null) {
            adServers.append(line)
            adServers.append("\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                        super.onReceivedError(view, request, error)
                        if (error.errorCode == ERROR_HOST_LOOKUP) {
                            view.loadData("<html><body><h2>Internet connection is lost. Please check your network settings.</h2></body></html>", "text/html", null)
                        }
                    }
                }
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Enable JavaScript
                settings.javaScriptEnabled = true

                // Enable DOM storage
                settings.domStorageEnabled = true

                // Enable built-in zoom controls
                settings.builtInZoomControls = true
                settings.displayZoomControls = false

                // Set user agent to desktop mode
                settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36"

                webChromeClient = object : WebChromeClient() {
                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        if (view is FrameLayout) {
                            // If a view already exists then immediately terminate the new one
                            callback?.onCustomViewHidden()
                            return
                        }
                    }

                    override fun onHideCustomView() {
                        // Remove the custom view and restore the WebView
                    }
                }

                // Handle page navigation
                webViewClient = object : WebViewClient() {
                    // This line prevents WebView from navigating to other pages
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        return true
                    }
                    override fun shouldInterceptRequest(
                        view: WebView,
                        request: WebResourceRequest
                    ): WebResourceResponse? {
                        val empty = ByteArrayInputStream("".toByteArray())
                        val kk5 = adServers.toString()
                        if (kk5.contains(":::::" + request.url.host))
                            return WebResourceResponse("text/plain", "utf-8", empty)
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                        super.onReceivedError(view, request, error)

                        // Check if the error is related to internet disconnection
                        if (error.errorCode == ERROR_HOST_LOOKUP) {
                            // Load a local error page or show an error message
                            view.loadData("<html><body><h2>Internet connection is lost. Please check your network settings.</h2></body></html>", "text/html", null)
                        }
                    }
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        view.evaluateJavascript(
                            "document.querySelector('.servers').style.display='none';",
                            null
                        )
                    }

                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        return if (view.url == url) {
                            view.loadUrl(url)
                            true
                        } else {
                            // Prevent loading URLs not from the current URL
                            false
                        }
                    }
                }

                loadUrl("https://lalremlian.github.io/privacy_policy/")
//                loadData(frameVideo.value, "text/html", "utf-8")
            }
        },
//        update = { webView ->
////            webView.loadUrl("https://lalremlian.github.io/privacy_policy/")
//            webView.loadUrl("https://vidsrc.xyz/embed/movie/tt5433140")
//        }
    )
}