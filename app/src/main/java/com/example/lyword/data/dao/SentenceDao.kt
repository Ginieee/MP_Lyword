package com.example.lyword.data.dao

import androidx.room.*
import com.example.lyword.data.entity.SentenceEntity

@Dao
interface SentenceDao {
    @Insert
    fun insertSentence(sentence : SentenceEntity) : Long

    @Delete
    fun deleteSentence(sentence : SentenceEntity)

    @Update
    fun updateSentence(sentence : SentenceEntity)

    @Insert
    fun insertSentenceList(list : List<SentenceEntity>) : List<Long>

    @Query("SELECT * FROM sentence_table WHERE sentenceId IN (:ids)")
    fun getSentencesById(ids : List<Long>) : List<SentenceEntity>
}