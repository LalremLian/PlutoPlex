package com.lazydeveloper.plutoplex.presentation.screen.player

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.google.accompanist.web.rememberSaveableWebViewState
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.plutoplex.presentation.composables.CustomAlertDialog
import com.lazydeveloper.plutoplex.util.CommonEnum
import com.lazydeveloper.plutoplex.presentation.composables.findActivity
import com.lazydeveloper.plutoplex.util.getSharedPrefs
import com.lazydeveloper.plutoplex.presentation.composables.showInterstitial
import com.lazydeveloper.plutoplex.presentation.composables.showInterstitial2
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SeriesPlayerScreen(
    navController: NavController,
    id: Int,
    seasonId: Int,
    episodeId: Int,
    serverId: Int
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val webView = remember { WebView(context) }
    val currentUrl = remember { mutableStateOf("") }
    val state = rememberSaveableWebViewState()

    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            // Handle the situation when there's no web page to go back to
            // For example, you can navigate to another screen
        }
    }

    when (serverId) {
        1 -> TvWebViewPage2(
            url = "${
                mPref.getString(
                    CommonEnum.SERVER_ONE.toString(),
                    ""
                )
            }$id&tmdb=1&s=$seasonId&e=$episodeId",
            navController
        )

        //vidsrc
        else -> TvWebViewPage(
            url = "${
                mPref.getString(
                    CommonEnum.SERVER_TWO_TV.toString(),
                    ""
                )
            }$id/$seasonId/$episodeId",
            navController
        )
    }

    LaunchedEffect(state.lastLoadedUrl) {
        currentUrl.value = state.lastLoadedUrl.toString()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TvWebViewPage(
    url: String,
    navController: NavController
) {
    Log.e("URL", url)
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    var isExitDialogVisible = rememberSaveable { mutableStateOf(false) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var isTimeOver by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(900000) // delay for 15 minute
        isTimeOver = true
    }

    //.................................................
    // Compose WebView Part 9 | Removes or Stop Ad in web

    val adServers = StringBuilder()
    var line: String? = ""
    val inputStream = context.resources.openRawResource(R.raw.adblockserverlist)
    val br = BufferedReader(InputStreamReader(inputStream))
    try {
        while (br.readLine().also { line = it } != null) {
            adServers.append(line)
            adServers.append("\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    BackHandler { isExitDialogVisible.value = true }

    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to exit?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {
            if (mPref.getString(CommonEnum.SHOW_EXIT_ADS.toString(), "false") == "true") {
                if (isTimeOver) {
                    showInterstitial(
                        context,
                        onAdDismissed = {
                            navController.popBackStack()
                        },
                        onError = {
                            showInterstitial2(
                                context,
                                onAdDismissed = {
                                    navController.popBackStack()
                                },
                                onError = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    )
                } else {
                    navController.popBackStack()
                }
            } else {
                navController.popBackStack()
            }
        },
        onDismissClick = {
            isExitDialogVisible.value = false
        }
    )

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
//                settings.setSupportZoom(true)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                // Enable built-in zoom controls
//                settings.builtInZoomControls = true
//                settings.displayZoomControls = false

                // Set user agent to desktop mode
//                settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36"

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
//                loadUrl("https://lalremlian.github.io/privacy_policy/")
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )

    DisposableEffect(context) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@DisposableEffect onDispose {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.apply {
                hide(WindowInsetsCompat.Type.statusBars())
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            onDispose {
                webView?.apply {
                    clearCache(true)
                    clearHistory()
                    clearFormData()
                }
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        } else {
            // For Android versions below R, use deprecated methods
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            onDispose {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TvWebViewPage2(
    url: String,
    navController: NavController
) {
    Log.e("URL", url)
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    var isExitDialogVisible = rememberSaveable { mutableStateOf(false) }
    var webView: WebView? by remember { mutableStateOf(null) }
    val adServers = StringBuilder()
    var line: String? = ""
    val inputStream = context.resources.openRawResource(R.raw.adblockserverlist)
    val br = BufferedReader(InputStreamReader(inputStream))
    var isTimeOver by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(900000) // delay for 15 minute
        isTimeOver = true
    }

    try {
        while (br.readLine().also { line = it } != null) {
            adServers.append(line)
            adServers.append("\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    BackHandler { isExitDialogVisible.value = true }

    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to exit?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = {
            if (mPref.getString(CommonEnum.SHOW_EXIT_ADS.toString(), "false") == "true") {
                if (isTimeOver) {
                    showInterstitial(
                        context,
                        onAdDismissed = {
                            navController.popBackStack()
                        },
                        onError = {
                            showInterstitial2(
                                context,
                                onAdDismissed = {
                                    navController.popBackStack()
                                },
                                onError = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    )
                } else {
                    navController.popBackStack()
                }
            } else {
                navController.popBackStack()
            }
        },
        onDismissClick = {
            isExitDialogVisible.value = false
        }
    )

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()

                // Enable JS
                settings.javaScriptEnabled = true
                webView?.evaluateJavascript(
                    "document.querySelector('logo').style.display='none';",
                    null
                )

                // Add a custom WebViewClient to intercept requests
                webViewClient = object : WebViewClient() {
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

                    override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceError
                    ) {
                        super.onReceivedError(view, request, error)

                        // Check if the error is related to internet disconnection
                        if (error.errorCode == ERROR_HOST_LOOKUP) {
                            // Load a local error page or show an error message
                            view.loadData(
                                "<html><body><h2>Internet connection is lost. Please check your network settings.</h2></body></html>",
                                "text/html",
                                null
                            )
                        }
                    }
                }

                // Set up back navigation
                setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP && canGoBack()) {
                        goBack()
                        true
                    } else {
                        false
                    }
                }

                loadUrl(url)

                // Set WebChromeClient to enable fullscreen
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
                webView = this
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(context) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@DisposableEffect onDispose {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.apply {
                hide(WindowInsetsCompat.Type.statusBars())
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            onDispose {
                insetsController.apply {
                    show(WindowInsetsCompat.Type.statusBars())
                    show(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        } else {
            // For Android versions below R, use deprecated methods
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            onDispose {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}
