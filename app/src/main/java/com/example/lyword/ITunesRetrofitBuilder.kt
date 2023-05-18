package com.example.lyword

import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ITunesApiClient {
    private const val ITUNES_BASE_URL = "https://itunes.apple.com"

    fun getITunesRetrofit() : Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}