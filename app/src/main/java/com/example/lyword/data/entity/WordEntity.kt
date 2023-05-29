package com.example.lyword.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    var wordId : Long = 0,

    @ColumnInfo(name = "word_sentence_idx")
    var wordSentenceIdx : Int = 0,

    @ColumnInfo(name = "word_origin")
    var wordOrigin : String = "",

    @ColumnInfo(name = "word_pronunciation")
    var wordPronunciation : String = "",

    @ColumnInfo(name = "word_english")
    var wordEnglish : String = ""
)
