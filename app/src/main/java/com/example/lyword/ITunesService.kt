package com.example.lyword

import android.util.Log
import com.example.lyword.ITunesApiClient.getITunesRetrofit
import retrofit2.Call
import retrofit2.Response
import retrofit2.create

class ITunesService {
    private lateinit var iTunesView : ITunesView

    fun setITunesView(iTunesView : ITunesView) {
        this.iTunesView = iTunesView
    }

    fun getSearchResult(search : String) {
        val iTunesService = getITunesRetrofit().create(ITunesRetroInterface::class.java)

        iTunesService.getSearchResult(search, "KR", "music", "song", 200).enqueue(object : retrofit2.Callback<ITunesResponse> {
            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                val resp : ITunesResponse? = response.body()
                Log.d("ITUNES_SEARCH", resp.toString())
                if (resp != null) {
                    iTunesView.onSearchITunesSuccess(resp.resultCount, resp.results)
                } else {
                    iTunesView.onSearchITunesSuccess(100, null)
                }

            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                Log.d("ITUNES_SEARCH_FAIL", t.message.toString())
            }
        })

        Log.d("ITUNES_SEARCH", "iTunes Search Finish")
    }
}