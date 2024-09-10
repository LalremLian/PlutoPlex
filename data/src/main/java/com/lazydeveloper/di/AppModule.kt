package com.lazydeveloper.di

import com.lazydeveloper.network.ApiClient
import com.lazydeveloper.network.api.ApiInterfaceSecond
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideInterfaceSecond(): ApiInterfaceSecond {
        return ApiClient.getSecondRetrofit().create(ApiInterfaceSecond::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideInterfaceFTTP(): ApiInterfaceFTTP {
//        return ApiClient.getTFFPRetrofit().create(ApiInterfaceFTTP::class.java)
//    }
}
