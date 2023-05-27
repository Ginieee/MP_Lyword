package com.example.lyword.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentence_table")
data class SentenceEntity (
    @PrimaryKey(autoGenerate = true)
    var sentenceId : Long = 0,

    @ColumnInfo(name = "sentence_origin")
    var sentenceOrigin : String = "",

    @ColumnInfo(name = "sentence_pronunciation")
    var wordPronunciation : String = "",

    @ColumnInfo(name = "sentence_english")
    var wordEnglish : String = ""
)