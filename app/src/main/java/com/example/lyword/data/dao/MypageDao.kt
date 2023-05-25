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
}