package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lyword.data.entity.WordEntity

@Dao
interface WordDao {
    @Query("Select * From WordEntity")
    fun getWord(): List<WordEntity>

    @Query("SELECT * FROM WordEntity WHERE word_sentence_idx = :lyricsId")
    fun getWordByLyricsId(lyricsId: Int): List<WordEntity>

    @Insert
    fun insertWord(wordEntity: WordEntity)

    @Insert
    fun insertWords(wordEntity: List<WordEntity>)
}