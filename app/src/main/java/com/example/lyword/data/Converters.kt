package com.example.lyword.data

import androidx.room.TypeConverter
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.WordEntity
import com.google.gson.Gson

class SentenceListConverters {
    @TypeConverter
    fun listToJson(value : List<SentenceEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value : String?) = Gson().fromJson(value, Array<SentenceEntity?>::class.java).toList()
}

class WordListConverters {
    @TypeConverter
    fun listToJson(value : List<WordEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value : String?) = Gson().fromJson(value, Array<WordEntity?>::class.java).toList()
}