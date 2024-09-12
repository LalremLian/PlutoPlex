package com.lazydeveloper.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.google.gson.Gson
import com.lazydeveloper.network.model.FireStoreServer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String

const val SESSION_PREF_NAME = "PREF_SESSION_DATA"

@Singleton
class SessionPreference(
    @ApplicationContext context: Context,
    val mSharedPreferences: SharedPreferences,
) {
    val json: Gson = Gson()
    var mEditor: SharedPreferences.Editor = mSharedPreferences.edit()
    val appThemeState: MutableStateFlow<Boolean> = MutableStateFlow(isDarkTheme)
    var isDarkTheme: Boolean
        get() = get("PREF_APP_THEME", true)
        set(themeMode) {
            set("PREF_APP_THEME", themeMode)
        }

    var appVersion: String
        get() = mSharedPreferences.getString(APP_VERSION, "") ?: ""
        set(appVersion) {
            mSharedPreferences.edit().putString(APP_VERSION, appVersion).apply()
        }

    var tmdbImagePath: String
        get() = mSharedPreferences.getString(TMDB_IMAGE_PATH, "") ?: ""
        set(tmdbImagePath) {
            mSharedPreferences.edit().putString(TMDB_IMAGE_PATH, tmdbImagePath).apply()
        }

    var tmdbImagePathBackCover: String
        get() = mSharedPreferences.getString(TMDB_IMAGE_PATH_BACK_COVER, "") ?: ""
        set(tmdbImagePathBackCover) {
            mSharedPreferences.edit().putString(TMDB_IMAGE_PATH_BACK_COVER, tmdbImagePathBackCover).apply()
        }

    var telegramLink: String
        get() = mSharedPreferences.getString(TELEGRAM_LINK, "") ?: ""
        set(telegramLink) {
            mSharedPreferences.edit().putString(TELEGRAM_LINK, telegramLink).apply()
        }

    var serverOne: String
        get() = mSharedPreferences.getString(SERVER_ONE, "") ?: ""
        set(serverOne) {
            mSharedPreferences.edit().putString(SERVER_ONE, serverOne).apply()
        }

    var serverTwo: String
        get() = mSharedPreferences.getString(SERVER_TWO, "") ?: ""
        set(serverTwo) {
            mSharedPreferences.edit().putString(SERVER_TWO, serverTwo).apply()
        }

    var serverTwoTv: String
        get() = mSharedPreferences.getString(SERVER_TWO_TV, "") ?: ""
        set(serverTwoTv) {
            mSharedPreferences.edit().putString(SERVER_TWO_TV, serverTwoTv).apply()
        }

    var showAdmobBanner: Boolean
        get() = mSharedPreferences.getBoolean(BANNER, false)
        set(showAdmobBanner) {
            mSharedPreferences.edit().putBoolean(BANNER, showAdmobBanner).apply()
        }

    var appUrl: String
        get() = mSharedPreferences.getString(APP_URL, "") ?: ""
        set(appUrl) {
            mSharedPreferences.edit().putString(APP_URL, appUrl).apply()
        }

    var showExitAds: Boolean
        get() = mSharedPreferences.getBoolean(SHOW_EXIT_ADS, false)
        set(showExitAds) {
            mSharedPreferences.edit().putBoolean(SHOW_EXIT_ADS, showExitAds).apply()
        }

    var blogSite: String
        get() = mSharedPreferences.getString(BLOG_SITE, "") ?: ""
        set(blogSite) {
            mSharedPreferences.edit().putString(BLOG_SITE, blogSite).apply()
        }

    var visitUs: String
        get() = mSharedPreferences.getString(VISIT_US, "") ?: ""
        set(visitUs) {
            mSharedPreferences.edit().putString(VISIT_US, visitUs).apply()
        }

    var selectedEpisode: Int
        get() = get(SELECTED_EPISODE, 0)
        set(value) {
            set(SELECTED_EPISODE, value)
        }

    fun saveFirebaseData(firebaseData: FireStoreServer) {
        tmdbImagePath = firebaseData.tmdbImagePath
        tmdbImagePathBackCover = firebaseData.tmdbImagePathBackCover
        telegramLink = firebaseData.telegramLink
        serverOne = firebaseData.serverOne
        serverTwo = firebaseData.serverTwo
        serverTwoTv = firebaseData.serverTwoTv
        showAdmobBanner = firebaseData.showAdmobBanner
        appUrl = firebaseData.appUrl
        showExitAds = firebaseData.showExitAds
        blogSite = firebaseData.blogSite
        visitUs = firebaseData.visitUs
    }

    companion object {
        private var instance: SessionPreference? = null

        const val SELECTED_EPISODE = "selected_episode"

        const val APP_VERSION = "app_version"

        const val TMDB_IMAGE_PATH = "tmdb_image_path"
        const val TMDB_IMAGE_PATH_BACK_COVER = "tmdb_image_path_back_cover"
        const val TELEGRAM_LINK = "telegram_link"
        const val SERVER_ONE = "server_one"
        const val SERVER_TWO = "server_two"
        const val SERVER_TWO_TV = "server_two_tv"
        const val BANNER = "banner"
        const val APP_URL = "app_url"
        const val SHOW_EXIT_ADS = "show_exit_ads"
        const val BLOG_SITE = "blog_site"
        const val VISIT_US = "visit_us"

        fun init(mContext: Context) {
            if (instance == null) {
                instance = SessionPreference(
                    mContext,
                    mContext.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE)
                )
            }
        }
        
        fun getInstance(): SessionPreference {
            if (instance == null) {
                throw InstantiationException("Instance is null...call init() first")
            }
            return instance as SessionPreference
        }
    }
    
    inline fun <reified T : Any> set(key: String, value: T) {
        val encryptedKey = encode(key)
        val encodedValue = encode(value.toString())
        mEditor.putString(encryptedKey, encodedValue).also { mEditor.apply() }
    }
    
    inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T {
        val encodedKey = encode(key)
        var value = mSharedPreferences.getString(encodedKey, "default")
        value = if (value == "default") {
            defaultValue?.toString() ?: when(T::class) {
                String::class -> ""
                Int::class -> "0"
                Long::class -> "0"
                Boolean::class -> "false"
                else -> throw IllegalArgumentException("This default value type is not accepted only pass String, Long, Int, Boolean.")
            }
        } else {
            decode(value!!)
        }
        return when (T::class) {
            String::class -> value as T
            Int::class -> value.toInt() as T
            Long::class -> value.toLong() as T
            Boolean::class -> value.toBoolean() as T
            else -> throw IllegalArgumentException("This default value type is not accepted only pass String, Long, Int, Boolean.")
        }
    }
    
    fun encode(plain: String): String {
        val b64encoded = Base64.encodeToString(plain.toByteArray(), Base64.DEFAULT)
        // Reverse the string
        val reverse = StringBuffer(b64encoded).reverse().toString()
        val tmp = StringBuilder()
        val offset = 4
        for (element in reverse) {
            tmp.append((element.code + offset).toChar())
        }
        return tmp.toString()
    }
    
    fun decode(secret: String): String {
        val tmp = StringBuilder()
        val offset = 4
        for (element in secret) {
            tmp.append((element.code - offset).toChar())
        }
        val reversed = StringBuffer(tmp.toString()).reverse().toString()
        return String(Base64.decode(reversed, Base64.DEFAULT))
    }
    
    inline fun <reified T> clearAndRestore(vararg restore: Pair<String, T>) {
        mEditor.clear()
        mEditor.apply()
        restore.forEach {
            when (T::class) {
                String::class -> set(it.first, it.second as String)
                Int::class -> set(it.first, it.second as Int)
                Long::class -> set(it.first, it.second as Long)
                Boolean::class -> set(it.first, it.second as Boolean)
                else -> throw IllegalArgumentException("This value type is not accepted. Pass only String, Long, Int, Boolean.")
            }
        }
    }
    
    fun clearAll() {
        mEditor.clear()
        mEditor.apply()
    }

}
//fun Context.clearPref() {
//    val prefs = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
//    val editor = prefs.edit()
//    editor?.clear()?.apply()
//}