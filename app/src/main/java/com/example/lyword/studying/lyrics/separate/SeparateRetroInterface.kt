package com.example.lyword.studying.lyrics.separate

import com.example.lyword.BuildConfig.SEPARATE_KEY
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface SeparateRetroInterface {
    @Headers(
        "Content-Type: application/json; charset=UTF-8",
        "Authorization: $SEPARATE_KEY"
    )
    @POST("/WiseNLU_spoken")
    fun getSeparateList(
        @Body requestBody : SeparateRequest
    ) : retrofit2.Call<SeparateResponse>
}