package com.lazydeveloper.plutoplex.presentation.screen.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore
import com.lazydeveloper.plutoplex.R
import com.lazydeveloper.network.model.FireStoreServer
import com.lazydeveloper.plutoplex.navigation.Graph
import com.lazydeveloper.plutoplex.navigation.Screen
import com.lazydeveloper.plutoplex.presentation.composables.CustomImage
import com.lazydeveloper.plutoplex.presentation.composables.CustomText
import com.lazydeveloper.plutoplex.ui.theme.Background_Black
import com.lazydeveloper.plutoplex.ui.theme.Loading_Orange
import com.lazydeveloper.plutoplex.util.CommonEnum
import com.lazydeveloper.plutoplex.util.getSharedPrefs
import com.lazydeveloper.plutoplex.util.getVersionName
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await


private fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    )
}

//private fun initialOneSignal(context: Context) {
//    // Verbose Logging set to help debug issues, remove before releasing your app.
//    OneSignal.Debug.logLevel = LogLevel.VERBOSE
//    // OneSignal Initialization
//    OneSignal.initWithContext(context, ONESIGNAL)
//    // requestPermission will show the native Android notification permission prompt.
//    CoroutineScope(Dispatchers.IO).launch {
//        OneSignal.Notifications.requestPermission(true)
//    }
//}


@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun SplashScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val snackbarHostState = remember { SnackbarHostState() }
    val systemUiController = rememberSystemUiController()
    val isInternetOn = isInternetConnected(context)
    var isServerAvailable by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("Hello") }
    val db = FirebaseFirestore.getInstance()
    var response: FireStoreServer

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons (preferred, as we're using a light status bar).
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )
    }

    if (isInternetOn) {
        LaunchedEffect(Unit) {
            try {
                val data = db.collection("data").get().await()
                data.map {
                    response = it.toObject(FireStoreServer::class.java)
                    if (context.getVersionName() == response.version) {
                        mPref.edit().putString(CommonEnum.VERSION.toString(), context.getVersionName()).apply()
                    } else {
                        mPref.edit().putString(CommonEnum.VERSION.toString(), "").apply()
                    }

                    mPref.edit().apply {
                        putString(CommonEnum.TMDB_IMAGE_PATH.toString(), response.tmdbImagePath)
                        putString(CommonEnum.TMDB_IMAGE_PATH_BACK_COVER.toString(), response.tmdbImagePathBackCover)
                        putString(CommonEnum.TELEGRAM_LINK.toString(), response.telegramLink)
                        putString(CommonEnum.SERVER_ONE.toString(), response.serverOne)
                        putString(CommonEnum.SERVER_TWO.toString(), response.serverTwo)
                        putString(CommonEnum.SERVER_TWO_TV.toString(), response.serverTwoTv)
                        putString(CommonEnum.BANNER.toString(), response.showAdmobBanner.toString())
                        putString(CommonEnum.APP_URL.toString(), response.appUrl)
                        putString(CommonEnum.SHOW_EXIT_ADS.toString(), response.showExitAds.toString())
                        putString(CommonEnum.BLOG_SITE.toString(), response.blogSite)
                        putString(CommonEnum.VISIT_US.toString(), response.visitUs)
                        apply()
                    }

                    if (response.server) {
                        isServerAvailable = true
                        delay(1200).apply {
                            navController.navigate(Graph.HomeGraph.route) {
                                popUpTo(Screen.SplashScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    } else {
                        message = response.description
                        isServerAvailable = false
                    }
                }
            } catch (e: Exception) {
                Log.e("FirebaseInit", "onCreate: ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Background_Black)
    ) {
        CustomImage(
            imageId = R.drawable.background,
            contentDescription = "Category Screen",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomImage(
                imageId = R.drawable.ic_logo,
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
            )
            CustomImage(
                imageId = R.drawable.ic_title,
                contentDescription = "logo",
                modifier = Modifier
                    .height(34.dp)
            )
            Spacer(modifier = Modifier.size(14.dp))

            if (isInternetOn) {
                LinearProgressIndicator(
                    color = Loading_Orange,
                    backgroundColor = Color(0xFF424242),
                    modifier = Modifier
                        .width(100.dp),
                )

            } else {
                Text(
                    text = "No network connection. Please check your network settings and try again.",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }

            if (!isServerAvailable) {
                Text(
                    text = message,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .padding(top = 20.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                ServerErrorBody(navController)

            }

        }
        CustomSnackBar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        CustomText(
            text = "Developed by",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(bottom = 80.dp)
                .align(Alignment.BottomCenter)
        )
        CustomImage(
            imageId = R.drawable.lazy_logo,
            contentDescription = "lazy_logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    ) {
        Snackbar(
            snackbarData = it,
            contentColor = Color.White,
            backgroundColor = Color(0xFF1D1D1D)
        )
    }
}

@Composable
fun ServerErrorBody(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(top = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomImage(
                imageId = R.drawable.ic_telegram,
                contentDescription = "telegram",
                modifier = Modifier
                    .size(25.dp)
            )
            CustomText(
                text = "Join us on Telegram for an update.",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )
        }
        CustomText(
            text = "Click here",
            color = Color(0xC42196F3),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)
                .clickable {
                    openTelegram(navController)
                }
        )
    }
}

fun openTelegram(navController: NavController) {
    val context = navController.context
    val mPref = context.getSharedPrefs()
    val uri = Uri.parse(mPref.getString(CommonEnum.TELEGRAM_LINK.toString(), ""))
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val packageManager = navController.context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
        navController.context.startActivity(intent)
    } else {
        Toast.makeText(
            navController.context,
            "Browser or Telegram not installed",
            Toast.LENGTH_SHORT
        ).show()
    }
}
