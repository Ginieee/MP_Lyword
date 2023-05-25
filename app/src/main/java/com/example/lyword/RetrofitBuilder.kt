package com.example.lyword.home.search

import android.util.Log
import com.example.lyword.BuildConfig.SEPARATE_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ITunesApiClient {
    private const val ITUNES_BASE_URL = "https://itunes.apple.com"

    fun getITunesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

//val client = OkHttpClient.Builder()
//    .addInterceptor { chain ->
//        val originalRequest = chain.request()
//        val modifiedRequest = originalRequest.newBuilder()
//            .addHeader("Authorization", SEPARATE_KEY)
//            .addHeader("Content-Type", "application/json")
//            .build()
//
//        Log.d("HEADER", "Request Headers : ${modifiedRequest.headers()}")
//
//        chain.proceed(modifiedRequest)
//    }
//    .build()

val client = OkHttpClient.Builder()
    .addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val headers = request.headers
            for (i in 0 until headers.size) {
                val name = headers.name(i)
                val value = headers.value(i)
                Log.d("Header", "$name: $value")
            }
            return chain.proceed(request)
        }
    })
    .build()

object SeparateApiClient {
    private const val SEPARATE_BASE_URL = "http://aiopen.etri.re.kr:8000"

    fun getSeparateRetrofit(): Retrofit {

        // 로깅인터셉터 세팅
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply{ interceptor.level = HttpLoggingInterceptor.Level.BODY }
        // OKHttpClient에 로깅인터셉터 등록
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(20000L, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(SEPARATE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}