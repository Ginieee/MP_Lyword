package com.example.lyword.home.search

interface ITunesView {
    fun onSearchITunesSuccess(count : Int, result : List<ITunesResult>?)
}