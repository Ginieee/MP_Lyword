package com.example.lyword.data.dao

import androidx.room.*

@Dao
interface StudyDao {
    @Insert
    fun insertStudy(study : StudyDao) : Long

    @Delete
    fun deleteStudy(study : StudyDao)

    @Update
    fun updateStudy(study : StudyDao)

    @Query("SELECT * FROM study_table")
    fun getStudyList() : List<StudyDao>
}