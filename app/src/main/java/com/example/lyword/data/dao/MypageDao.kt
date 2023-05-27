package com.example.lyword.data.dao

import androidx.room.*
import com.example.lyword.data.entity.MypageEntity

@Dao
interface MypageDao {
    @Insert
    fun insertMypage(mypage : MypageEntity) : Long

    @Delete
    fun deleteMypage(mypage : MypageEntity)

    @Update
    fun updateMypage(mypage : MypageEntity)

    @Query("SELECT * FROM MypageEntity ORDER BY mypageId DESC LIMIT 1")
    suspend fun getLatestMypage(): MypageEntity?

    @Query("UPDATE MypageEntity SET name = :name, introduction = :introduction, profile_img = :profileImg WHERE mypageId = 1")
    fun updateMypageset(name: String, introduction: String, profileImg: String)
}