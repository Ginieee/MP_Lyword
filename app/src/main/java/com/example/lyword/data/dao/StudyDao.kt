package com.example.lyword.data.dao

import androidx.room.*
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity

@Dao
interface StudyDao {
    @Insert
    fun insertStudy(study : StudyEntity) : Long

    @Delete
    fun deleteStudy(study : StudyEntity)

    @Update
    fun updateStudy(study : StudyEntity)

    @Query("SELECT * FROM study_table")
    fun getStudyList() : List<StudyEntity>

//    @Query("SELECT COUNT(*) FROM study_table WHERE studyId = :studyId")
//    fun hasStudy(studyId: Long): Int
//
//    @Update
//    suspend fun updateSentenceList(sentenceList: List<SentenceEntity>)
    @Query("SELECT * FROM study_table WHERE percentage_num = 100")
    fun getCompletedStudies(): List<StudyEntity>

    @Query("SELECT * FROM study_table WHERE percentage_num < 99")
    fun getInProgressStudies(): List<StudyEntity>

    @Query("SELECT COUNT(*) FROM study_table WHERE studyId = :studyId")
    fun hasStudy(studyId: Long): Int

    @Update
    suspend fun updateSentenceList(sentenceList: List<SentenceEntity>)

    @Query("SELECT * FROM study_table WHERE studyId = :studyId")
    fun getStudyById(studyId : Long) : StudyEntity
}