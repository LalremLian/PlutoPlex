package com.lazydeveloper.trelloplex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.install.model.AppUpdateType
import com.lazydeveloper.trelloplex.navigation.RootNavGraph
import com.lazydeveloper.trelloplex.ui.theme.CLVPlexTheme
import com.lazydeveloper.trelloplex.presentation.composables.loadInterstitial
import com.lazydeveloper.trelloplex.presentation.composables.removeInterstitial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        if(updateType == AppUpdateType.FLEXIBLE){
//            appUpdateManager.registerListener(installStateUpdatedListener)
//        }
//        checkForAppUpdate()

        MobileAds.initialize(this) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("B3EEABB8EE11C2BE770B684D95219ECB")).build()
        )
        loadInterstitial(this)

        setContent {
            CLVPlexTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .paint(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.Crop
                        )
                ) {
                    RootNavGraph(navHostController = rememberNavController())
                }
            }
        }
//        val resultLauncher: ActivityResultLauncher<Intent> =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//           if (result.resultCode != Activity.RESULT_OK) {
//               Log.e("Update", "Update flow failed! Result code: ${result.resultCode}")
//           }
//        }
//        resultLauncher.launch(Intent(this, MainActivity::class.java))
    }
    fun showInterstialAd() {
        InterstitialAd.load(
            this,
            "ca-app-pub-9980622067720306/1923693784", //Change this with your own AdUnitID!
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(this@MainActivity)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
//        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
//            if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
//                val options = AppUpdateOptions.newBuilder(updateType).build()
//                appUpdateManager.startUpdateFlow(appUpdateInfo, this, options)
//            }
//        }
    }

    override fun onDestroy() {
        removeInterstitial()
        super.onDestroy()
//        if (updateType == AppUpdateType.FLEXIBLE) {
//            appUpdateManager.unregisterListener(installStateUpdatedListener)
//        }
    }

//    private val installStateUpdatedListener = InstallStateUpdatedListener { installState ->
//        if(installState.installStatus() == InstallStatus.DOWNLOADED){
//            Toast.makeText(this, "An update has been downloaded. Restarting the app in 3 seconds.", Toast.LENGTH_SHORT).show()
//            lifecycleScope.launch {
//                delay(3000)
//                appUpdateManager.completeUpdate()
//            }
//        }
//    }

//    private fun checkForAppUpdate() {
//        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
//            val isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//            val isUpdateTypeAllowed = when (updateType) {
//                AppUpdateType.FLEXIBLE -> appUpdateInfo.isFlexibleUpdateAllowed
//                else -> false
//            }
//            if(isUpdateAvailable && isUpdateTypeAllowed){
//                val options = AppUpdateOptions.newBuilder(updateType).build()
//                appUpdateManager.startUpdateFlow(appUpdateInfo, this, options)
//            }
//        }
//    }
}
