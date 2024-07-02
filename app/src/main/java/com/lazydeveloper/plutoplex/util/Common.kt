package com.lazydeveloper.plutoplex.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.content.BroadcastReceiver
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.IntentFilter
import android.os.Environment
import android.widget.Toast

fun View.backPress() {
    this.setOnClickListener {
        (this.context as Activity).finish()
    }
}

inline fun <reified T> Activity.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
}

inline fun <reified T> Context.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
}


inline fun <reified T> Activity.openActivityWithFinish(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
    finish()
}

inline fun <reified T> Fragment.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this.requireActivity(), T::class.java)
    intent.extras()
    startActivity(intent)
}
@SuppressLint("InlinedApi")
@RequiresApi(Build.VERSION_CODES.O)
fun Context.downloadFile(url: String, fileName: String) {
    try {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)

        // Register BroadcastReceiver to track completion
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (downloadID == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                    Toast.makeText(context, "Download Completed, Saved in Downloads folder", Toast.LENGTH_LONG).show()
                }
            }
        }

        this.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_NOT_EXPORTED)
    } catch (e: Exception) {
        Toast.makeText(this, "Source not found. Please try again later.", Toast.LENGTH_SHORT).show()
    }
}

fun Activity.goToAppPermissionSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, 111)
}

fun Long.toHhMmSs(): String {
    val seconds = (this / 1000).toInt()
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}

fun Activity.checkPhoneCallPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
}

infix fun ViewGroup.inflate(@LayoutRes view: Int): View {
    return LayoutInflater.from(context).inflate(view, this, false)
}

fun Int.inflate(viewGroup: ViewGroup): View {
    return LayoutInflater.from(viewGroup.context).inflate(this, viewGroup, false)
}

fun getAndroidID(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun Context.getVersionName(): String {
    return try {
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}

fun Context.getSharedPrefs(): SharedPreferences {
    return getSharedPreferences("my_app_shared_prefs", Context.MODE_PRIVATE)
}


fun SharedPreferences.putInt(key: String, value: Int) {
    edit().putInt(key, value).apply()
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

infix fun View.onClick(function: (View) -> Unit) {
    setOnClickListener {
        function.invoke(it)
    }
}
