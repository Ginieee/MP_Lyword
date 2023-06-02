package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.WordEntity

@Dao
interface WordDao {
    @Query("Select * From word_table")
    fun getWord(): List<WordEntity>

    @Query("SELECT * FROM word_table WHERE word_sentence_idx = :lyricsId")
    fun getWordByLyricsId(lyricsId: Long): List<WordEntity>

    @Insert
    fun insertWord(wordEntity: WordEntity): Long

    @Insert
    fun insertWordList(list : List<WordEntity>) : List<Long>

    @Insert
    fun insertWords(wordEntity: List<WordEntity>)

    @Query("SELECT * FROM word_table WHERE wordId IN (:ids)")
    fun getWordsById(ids : List<Long>) : List<WordEntity>
}