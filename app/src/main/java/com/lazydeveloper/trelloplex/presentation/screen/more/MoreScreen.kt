package com.lazydeveloper.trelloplex.presentation.screen.more

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lazydeveloper.trelloplex.R
import com.lazydeveloper.trelloplex.navigation.Screen
import com.lazydeveloper.trelloplex.presentation.composables.CustomImage
import com.lazydeveloper.trelloplex.presentation.composables.CustomItem
import com.lazydeveloper.trelloplex.presentation.composables.CustomText
import com.lazydeveloper.trelloplex.util.CommonEnum
import com.lazydeveloper.trelloplex.util.getSharedPrefs
import com.lazydeveloper.trelloplex.util.getVersionName

@Composable
fun MoreScreen(
    navController: NavController
) {
    MoreScreenBody(navController)
}

@Composable
fun MoreScreenBody(
    navController: NavController
) {
    val context = LocalContext.current
    val mPref = context.getSharedPrefs()
    val isEmpty = mPref.getString(CommonEnum.VERSION.toString(), "")
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CustomImage(
            imageId = R.drawable.ic_logo,
            contentDescription = "logo",
            modifier = Modifier
                .padding(top = 50.dp, bottom = 8.dp)
                .align(CenterHorizontally)
                .size(65.dp)
            )
        CustomImage(
            imageId = R.drawable.ic_title,
            contentDescription = "logo",
            modifier = Modifier
                .height(22.dp)
                .align(CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(18.dp))
//        CustomText(
//            text = stringResource(id = R.string.app_name),
//            color = Color(0xFFFFFFFF),
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            modifier = Modifier
//                .padding(bottom = 30.dp)
//                .align(CenterHorizontally)
//        )
        if(mPref.getString(CommonEnum.TELEGRAM_LINK.toString(),"") != ""){
            CustomItem(
                "Send Movie Request", R.drawable.ic_mail
            ) {
                openTelegram(navController)
            }
        }
        if(mPref.getString(CommonEnum.TELEGRAM_LINK.toString(),"") != ""){
            CustomItem(
                "Join us on Telegram", R.drawable.ic_support
            ) {
                openTelegram(navController)
            }
        }
        CustomItem(
            "History", R.drawable.ic_history
        ) {
            navController.navigate(Screen.HistoryScreen.route)
        }
        if(mPref.getString(CommonEnum.VISIT_US.toString(),"") != ""){
            CustomItem(
                "Visit Us", R.drawable.ic_web
            ) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(mPref.getString(CommonEnum.VISIT_US.toString(),""))
                context.startActivity(openURL)
            }
        }
        CustomItem(
            "Privacy Policy", R.drawable.ic_privacy
        ) {
            navController.navigate(Screen.PrivacyPolicyScreen.route)
        }
//        CustomItem(
//            "Copyright", R.drawable.ic_copyright
//        ) {
//            navController.navigate(Screen.CopyrightScreen.route)
//        }
        CustomText(
            text = "Version ${context.getVersionName()}",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .padding(bottom = 20.dp, top = 80.dp)
                .align(CenterHorizontally)
        )
    }
}

fun openTelegram(navController: NavController) {
    val context = navController.context
    val mPref = context.getSharedPrefs()

    val uri = Uri.parse(mPref.getString(CommonEnum.TELEGRAM_LINK.toString(),""))
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val packageManager = navController.context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
        navController.context.startActivity(intent)
    } else {
        Toast.makeText(navController.context, "Browser or Telegram not installed", Toast.LENGTH_SHORT).show()
    }
}