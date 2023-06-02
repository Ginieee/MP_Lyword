package com.example.lyword.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notify_table")
data class NotifyEntity(
    @PrimaryKey(autoGenerate = true)
    var notifyId : Long = 0,

    @ColumnInfo(name = "notify_title")
    var title : String = "",

    @ColumnInfo(name = "notify_content")
    var content : String = "",

    @ColumnInfo(name = "notify_img")
    var img : String? = "",

    @ColumnInfo(name = "notify_read")
    var isRead : Int = 0,

    @ColumnInfo(name = "notify_time")
    var time : String = ""
)
