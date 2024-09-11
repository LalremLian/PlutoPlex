package com.lazydeveloper.trelloplex.app

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        Log.e("ShortiflyApplication", "onCreate: ")
        FirebaseApp.initializeApp(this)
    }
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
//            .memoryCache {
//                MemoryCache.Builder(this)
//                    .maxSizePercent(0.20)
//                    .build()
//            }
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(cacheDir.resolve("image_cache"))
//                    .maxSizeBytes(5 * 1024 * 1024)
//                    .build()
//            }
//            .logger(DebugLogger())
//            .respectCacheHeaders(false)
            .build()
    }
}