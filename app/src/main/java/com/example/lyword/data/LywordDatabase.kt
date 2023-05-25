package com.example.lyword.data

import android.content.Context
import androidx.room.*
import com.example.lyword.data.dao.MypageDao
import com.example.lyword.data.dao.SentenceDao
import com.example.lyword.data.dao.StudyDao
import com.example.lyword.data.dao.WordDao
import com.example.lyword.data.entity.MypageEntity
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.data.entity.WordEntity

@Database(
    entities = [StudyEntity::class, SentenceEntity::class, WordEntity::class, MypageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        SentenceListConverters::class,
        WordListConverters::class
    ]
)
abstract class LywordDatabase: RoomDatabase() {
    abstract val studyDao : StudyDao
    abstract val sentenceDao : SentenceDao
    abstract val wordDao: WordDao
    abstract val myPageDao : MypageDao

    companion object{
        @Volatile
        private var INSTANCE: LywordDatabase? = null

        fun getInstance(context: Context): LywordDatabase?{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LywordDatabase::class.java,
                        "lyword-database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}