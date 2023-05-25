package com.example.lyword.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_table")
data class StudyEntity(
    @PrimaryKey(autoGenerate = true)
    var studyId : Long = 0,

    @ColumnInfo(name = "study_title")
    var title : String = "",

    @ColumnInfo(name = "study_artist")
    var artist : String = "",

    @ColumnInfo(name = "album_art")
    var album_art : String = "",

    @ColumnInfo(name = "percentage_num")
    var percent : Int = 0,

    @ColumnInfo(name = "sentence_list")
    var sentenceList : List<SentenceEntity>? = listOf(),

    @ColumnInfo(name = "word_list")
    var wordList : List<WordEntity>? = listOf()
)