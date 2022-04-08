package com.bibibla.appnote.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bibibla.appnote.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note : Note)

    @Delete
    suspend fun deleteNote(note : Note)

    @Query("DELETE FROM note")
    suspend fun deleteDatabaseNote()

    @Query("SELECT * FROM note")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE _isPin = 1")
    fun getNotesPin(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE _id = :id")
    suspend fun getNoteFromId(id : Int): Note

    @Query("SELECT * FROM note WHERE _tags LIKE :tagName")
    fun getNotesHadTagName(tagName: String): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE _dateDay = :dayOfMonth AND _dateMonth = :month AND _dateYear = :year")
    fun getNotesInTime(dayOfMonth:Int , month :Int , year : Int): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE _isSettingAlarm = :isSettingAlarm")
    fun getNotesAlert(isSettingAlarm:Boolean): LiveData<List<Note>>


}