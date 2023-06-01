package com.example.lyword.studying.lyrics.separate

import com.example.lyword.data.entity.StudyEntity

interface SeparateView {
    fun onGetLyricsSuccess(result: ArrayList<MorpResult>, index : Int)
}