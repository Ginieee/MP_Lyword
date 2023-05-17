package com.example.lyword

interface ITunesView {
    fun onSearchITunesSuccess(count : Int, result : List<ITunesResult>?)
}