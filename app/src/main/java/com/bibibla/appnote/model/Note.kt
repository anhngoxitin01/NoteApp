package com.bibibla.appnote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,
    @ColumnInfo(name = "_title")
    var title: String,
    @ColumnInfo(name = "_description")
    var description: String,
    @ColumnInfo(name = "_isPin")
    var isPin:Int = 0,                  // 0 is notPin , 1 is Pin
    @ColumnInfo(name = "_timeMinute")
    var timeMinute: Int? = null,
    @ColumnInfo(name = "_timeHour")
    var timeHour: Int? = null,
    @ColumnInfo(name = "_dateDay")
    var dateDay: Int? = null,
    @ColumnInfo(name = "_dateMonth")
    var dateMonth: Int? = null,
    @ColumnInfo(name = "_dateYear")
    var dateYear: Int? = null,
    @ColumnInfo(name = "_tags")
    var tags : String? = null
) : Serializable
