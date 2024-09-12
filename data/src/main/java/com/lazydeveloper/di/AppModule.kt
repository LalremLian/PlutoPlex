package com.lazydeveloper.di

import android.content.Context
import android.content.SharedPreferences
import com.lazydeveloper.network.ApiClient
import com.lazydeveloper.network.api.ApiInterfaceSecond
import com.lazydeveloper.data.preference.SESSION_PREF_NAME
import com.lazydeveloper.data.preference.SessionPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context.applicationContext
    }

    @Singleton
    @Provides
    fun provideInterfaceSecond(): ApiInterfaceSecond {
        return ApiClient.getSecondRetrofit().create(ApiInterfaceSecond::class.java)
    }

    @Provides
    @SessionSharedPreference
    fun providesSessionSharedPreference(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE)
    }

//    @Provides
//    @CommonSharedPreference
//    fun providesCommonSharedPreference(@ApplicationContext app: Context): SharedPreferences {
//        return app.getSharedPreferences(COMMON_PREF_NAME, Context.MODE_PRIVATE)
//    }

    @Provides
    @Singleton
    fun providesPreference(
        @SessionSharedPreference pref: SharedPreferences,
        @ApplicationContext ctx: Context
    ): SessionPreference {
        return SessionPreference(ctx, pref)
    }

//    @Provides
//    @Singleton
//    fun provideCommonPreference(
//        @CommonSharedPreference pref: SharedPreferences,
//        @ApplicationContext ctx: Context
//    ): CommonPreference {
//        return CommonPreference(ctx, pref)
//    }

//    @Singleton
//    @Provides
//    fun provideInterfaceFTTP(): ApiInterfaceFTTP {
//        return ApiClient.getTFFPRetrofit().create(ApiInterfaceFTTP::class.java)
//    }
}
