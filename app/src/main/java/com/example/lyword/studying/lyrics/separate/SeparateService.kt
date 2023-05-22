package com.example.lyword.studying.lyrics.separate

import android.util.Log
import com.example.lyword.home.search.SeparateApiClient.getSeparateRetrofit
import retrofit2.Call
import retrofit2.Response

class SeparateService {
    private lateinit var separateView : SeparateView

    fun setSeparateView(separateView: SeparateView) {
        this.separateView = separateView
    }

    fun getSeparateLyrics(separateRequest : SeparateRequest) {
        val separateService = getSeparateRetrofit().create(SeparateRetroInterface::class.java)
        Log.d("hi", separateRequest.toString())
        separateService.getSeparateList(separateRequest).enqueue(object : retrofit2.Callback<SeparateResponse> {
            override fun onResponse(
                call: Call<SeparateResponse>,
                response: Response<SeparateResponse>
            ) {
                Log.d("separate/success", response.toString())
                val resp : SeparateResponse? = response.body()
                Log.d("separate/success/resp", resp.toString())
                if (resp != null) {
                    val respToSentence: List<SentenceResult> = resp.returnObject.sentenceResult
                    var resMorp = ArrayList<MorpResult>()
                    for (sentence in respToSentence){
                        resMorp.addAll(sentence.morpList)
                    }

                    separateView.onGetLyricsSuccess(resMorp)
                }
            }

            override fun onFailure(call: Call<SeparateResponse>, t: Throwable) {
                Log.d("separate/failure", t.message.toString())
            }

        })
        Log.d("separate", "Separate Finish")
    }
}