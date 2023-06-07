package com.example.lyword.studying.lyrics.separate

import android.util.Log
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.home.search.SeparateApiClient.getSeparateRetrofit
import retrofit2.Call
import retrofit2.Response

class SeparateService {
    private lateinit var separateView : SeparateView

    fun setSeparateView(separateView: SeparateView) {
        this.separateView = separateView
    }

    // Call the Morpheme split API
    fun getSeparateLyrics(separateRequest: SeparateRequest, index: Int, isFinished: Int) {
        val separateService = getSeparateRetrofit().create(SeparateRetroInterface::class.java)
        var resMorp = ArrayList<MorpResult>()
        separateService.getSeparateList(separateRequest).enqueue(object : retrofit2.Callback<SeparateResponse> {
            override fun onResponse(
                call: Call<SeparateResponse>,
                response: Response<SeparateResponse>
            ) {
                Log.d("separate/success", response.toString())
                val resp : SeparateResponse? = response.body()
                Log.d("separate/success/resp", resp.toString())
                if (resp != null) {
                    // Get the result and store it in the list
                    val respToSentence: List<SentenceResult> = resp.returnObject.sentenceResult
                    // Get only Morpheme result
                    for (sentence in respToSentence){
                        resMorp.addAll(sentence.morpList)
                    }
                    // Call onGetLyrics if success
                    separateView.onGetLyricsSuccess(resMorp, index, isFinished)
                }
            }
            override fun onFailure(call: Call<SeparateResponse>, t: Throwable) {
                Log.d("separate/failure", t.message.toString())
            }
        })
    }
}