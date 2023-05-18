package com.example.lyword.home.search

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesRetroInterface {
    @GET("/search")
    fun getSearchResult(
        @Query("term") term : String,
        @Query("country") country : String,
        @Query("media") media : String,
        @Query("entity") entity : String,
        @Query("limit") limit : Int
    ) : retrofit2.Call<ITunesResponse>
}