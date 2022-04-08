package com.bibibla.appnote.repository

import androidx.lifecycle.LiveData
import com.bibibla.appnote.db.NoteDao
import com.bibibla.appnote.model.Note

class NoteRepository(private val noteDao : NoteDao) {

    suspend fun insertNote(note: Note){
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }

    suspend fun deleteDatabaseNote(){
        noteDao.deleteDatabaseNote()
    }

    suspend fun getNoteFromId(id: Int): Note {
        return noteDao.getNoteFromId(id)
    }

    fun getNotes(): LiveData<List<Note>>{
        return noteDao.getAllNotes()
    }

    fun getNotesPin(): LiveData<List<Note>>{
        return noteDao.getNotesPin()
    }

    fun getNotesHadTagName(tagName: String): LiveData<List<Note>>{
        return noteDao.getNotesHadTagName(tagName)
    }

    fun getNotesInTime(dayOfMonth:Int , month :Int , year : Int): LiveData<List<Note>>{
        return noteDao.getNotesInTime(dayOfMonth, month, year)
    }

    fun getNotesAlert(isSettingAlarm:Boolean): LiveData<List<Note>>{
        return noteDao.getNotesAlert(isSettingAlarm)
    }

}