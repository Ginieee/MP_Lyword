package com.example.lyword.studying.lyrics.separate

import com.google.gson.annotations.SerializedName

data class SeparateResponse(
    @SerializedName(value = "result") val result : Int,
    @SerializedName(value = "return_object") val returnObject : SeparateResult
)

data class SeparateResult(
    @SerializedName(value = "sentence") val sentenceResult : List<SentenceResult>
)

data class SentenceResult(
    @SerializedName(value = "morp") val morpList : List<MorpResult>
)

data class MorpResult(
    @SerializedName(value = "id") val id : Int,
    @SerializedName(value = "lemma") val text : String,
    @SerializedName(value = "type") val type : String
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
    val analysis_code : String,
    val text : String
)