package com.example.lyword.data.dao

import androidx.room.*
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
}