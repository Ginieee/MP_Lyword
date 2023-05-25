package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import org.snu.ids.kkma.ma.Sentence

@Dao
interface SentenceDao {
    @Insert
    fun insertSentence(sentence : Sentence) : Long

    @Delete
    fun deleteSentence(sentence : Sentence)

    @Update
    fun updateSentence(sentence : Sentence)
}