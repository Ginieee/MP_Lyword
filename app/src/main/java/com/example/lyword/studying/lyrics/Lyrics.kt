package com.example.lyword.studying.lyrics

data class Lyrics(
    var index: Int,
    var ly: String? = "",
    var pron: String? = "",
    var trans: String? = "",
)