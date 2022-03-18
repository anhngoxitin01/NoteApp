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

    suspend fun getNoteFromId(id: Int): Note {
        return noteDao.getNoteFromId(id)
    }

    fun getNotes(): LiveData<List<Note>>{
        return noteDao.getAllNotes()
    }

    fun getNotesPin(): LiveData<List<Note>>{
        return noteDao.getNotesPin()
    }

    fun getNotesHadTag(tag: String): LiveData<List<Note>>{
        return noteDao.getNotesHadTag(tag)
    }

}