package com.example.lyword.studying.lyrics.separate

import com.google.gson.annotations.SerializedName

data class SeparateResponse(
    @SerializedName(value = "request_id") val requestId : String,
    @SerializedName(value = "result") val result : Int,
    @SerializedName(value = "return_object") val return_object : List<SeparateResult>
)

data class SeparateResult(
    @SerializedName(value = "sentence") val sentenceList : List<Sentence>
)

data class Sentence(
    @SerializedName(value = "id") val id : Int,
    @SerializedName(value = "text") val text : String,
    @SerializedName(value = "word") val separateList : List<Separate>
)

data class Separate(
    @SerializedName(value = "id") val id : Int,
    @SerializedName(value = "text") val text : String,
    @SerializedName(value = "begin") val begin : Int,
    @SerializedName(value = "end") val end : Int
)

data class SeparateRequest(
    val requestId : String,
    val argument : SeparateArgument
)

data class SeparateArgument(
    val code : String,
    val text : String
)