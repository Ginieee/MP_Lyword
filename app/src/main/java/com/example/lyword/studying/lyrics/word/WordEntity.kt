package com.example.lyword.studying.lyrics.word

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity(
    val voc: String,
    val pron: String,
    val meaning: String,
    val songIndex: Int,
    val sentenceIndex: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
