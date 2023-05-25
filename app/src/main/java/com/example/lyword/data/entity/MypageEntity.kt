package com.example.lyword.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MypageEntity(
    @PrimaryKey(autoGenerate = true)
    var mypageId : Long = 0,

    @ColumnInfo(name = "name")
    var name : String = "",

    @ColumnInfo(name = "introduction")
    var introduction : String = "",

    @ColumnInfo(name = "profile_img")
    var profileImg : Int = 0,

    @ColumnInfo(name = "level")
    var level : Int = 0,

    @ColumnInfo(name = "completion")
    var completion : Int = 0,

    @ColumnInfo(name = "ongoing")
    var ongoing : Int = 0,
)