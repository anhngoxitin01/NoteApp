package com.bibibla.appnote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,
    @ColumnInfo(name = "_name")
    var name : String = "nameTag",
    @ColumnInfo(name = "_amount")
    var amount : Int = 1
) : Serializable
