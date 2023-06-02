package com.example.lyword.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lyword.data.entity.NotifyEntity

@Dao
interface NotifyDao {
    @Insert
    fun insertNotify(notify : NotifyEntity) : Long

    @Delete
    fun deleteNotify(notify : NotifyEntity)

    @Update
    fun updateNotify(notify : NotifyEntity)

    @Query("SELECT * FROM notify_table")
    fun getNotifyList() : List<NotifyEntity>

    @Query("SELECT * FROM notify_table WHERE notify_read = 0")
    fun getUnreadNotice() : List<NotifyEntity>

}