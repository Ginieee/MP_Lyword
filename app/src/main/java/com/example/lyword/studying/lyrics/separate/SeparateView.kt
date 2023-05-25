package com.example.lyword.studying.lyrics.separate

interface SeparateView {
    fun onGetLyricsSuccess(result: ArrayList<MorpResult>, index : Int)
}