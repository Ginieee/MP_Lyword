package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.lyword.data.entity.SentenceEntity
import org.snu.ids.kkma.ma.Sentence

@Dao
interface SentenceDao {
    @Insert
    fun insertSentence(sentence : SentenceEntity) : Long

    @Delete
    fun deleteSentence(sentence : SentenceEntity)

    @Update
    fun updateSentence(sentence : SentenceEntity)
}