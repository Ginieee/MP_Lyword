package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lyword.data.entity.WordEntity

@Dao
interface WordDao {
    @Query("Select * From WordEntity")
    fun getWord(): List<WordEntity>

    @Insert
    fun insertWord(wordEntity: WordEntity)

    @Query("SELECT COUNT(*) FROM WordEntity WHERE songIndex = :index")
    suspend fun hasSongIndex(index: Int): Int
}