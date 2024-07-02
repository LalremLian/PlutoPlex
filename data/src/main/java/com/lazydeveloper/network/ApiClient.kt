package com.lazydeveloper.network
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val TMDB_BASE_URL ="https://api.themoviedb.org"
//        private const val FTTP_BASE_URL ="http://103.145.232.246"

        private fun getClient(): OkHttpClient.Builder {
            return OkHttpClient().newBuilder()
                .callTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(getHttpLoggingInterceptor())
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

        fun getSecondRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient().build())
            .build()

//        fun getTFFPRetrofit(): Retrofit = Retrofit.Builder()
//            .baseUrl(FTTP_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(getClient().build())
//            .build()
    }
}