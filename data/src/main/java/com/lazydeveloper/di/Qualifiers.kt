package com.lazydeveloper.di

import javax.inject.Qualifier

@Qualifier
annotation class EncryptedHttpClient

@Qualifier
annotation class SimpleHttpClient

@Qualifier
annotation class DnsHttpClient

@Qualifier
annotation class AppCoroutineScope

@Qualifier
annotation class SessionSharedPreference

@Qualifier
annotation class CommonSharedPreference

@Qualifier
annotation class ToffeeHeader

@Qualifier
annotation class ApiHeader

@Qualifier
annotation class ExternalApiRetrofit

@Qualifier
annotation class PlainHttpClient

@Qualifier
annotation class CoilHttpClient

@Qualifier
annotation class ApplicationId

@Qualifier
annotation class AppVersionName

@Qualifier
annotation class AppVersionCode