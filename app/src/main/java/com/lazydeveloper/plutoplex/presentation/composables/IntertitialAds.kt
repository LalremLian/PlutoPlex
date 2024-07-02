package com.lazydeveloper.plutoplex.presentation.composables

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.lazydeveloper.plutoplex.R
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

var mInterstitialAd: InterstitialAd? = null
var mInterstitialAd2: InterstitialAd? = null
 var isAdShowing = mutableStateOf(false)

fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        "ca-app-pub-9980622067720306/5690855814", //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                // Load this interstitial2 if the first one failed
                loadInterstitial2(context)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        }
    )
}
fun loadInterstitial2(context: Context) {
    InterstitialAd.load(
        context,
        "ca-app-pub-9980622067720306/7379342008", //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd2 = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd2 = interstitialAd
            }
        }
    )
}
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}
fun showInterstitial(context: Context, onAdDismissed: () -> Unit, onError: () -> Unit) {
    isAdShowing.value = true
    val activity = context.findActivity()

    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                loadInterstitial(context)
                mInterstitialAd = null
                isAdShowing.value = false
                Log.e("TAG", "onAdFailedToShowFullScreenContent: ${e.message}")
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null

                loadInterstitial(context)
                onAdDismissed()
//                isAdShowing.value = false
                Log.e("TAG", "onAdDismissedFullScreenContent")
            }
        }
        mInterstitialAd?.show(activity)
    }else{
        onError()
    }
}

fun showInterstitial2(context: Context, onAdDismissed: () -> Unit, onError: () -> Unit) {
    val activity = context.findActivity()

    if (mInterstitialAd2 != null && activity != null) {
        mInterstitialAd2?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                loadInterstitial2(context)
                mInterstitialAd2 = null
                Log.e("TAG", "onAdFailedToShowFullScreenContent: ${e.message}")
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd2 = null

                loadInterstitial2(context)
                onAdDismissed()
//                isAdShowing.value = false
                Log.e("TAG", "onAdDismissedFullScreenContent")
            }
        }
        mInterstitialAd2?.show(activity)
    }else{
        onError()
    }
}

fun removeInterstitial() {
    mInterstitialAd?.fullScreenContentCallback = null
    mInterstitialAd = null
    mInterstitialAd2?.fullScreenContentCallback = null
    mInterstitialAd2 = null
}
