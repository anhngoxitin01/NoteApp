package com.bibibla.appnote.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.db.NoteDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.repository.NoteRepository
import com.bibibla.appnote.util.Event
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.log

class NoteViewModel(private val app : Application): ViewModel() {
    private val noteRepository = NoteRepository(
        NoteDatabase.getInstance(app.applicationContext , viewModelScope).getNoteDao()
    )

    val notes = noteRepository.getNotes()
    val notesPin = noteRepository.getNotesPin()
    var noteHadTag : LiveData<List<Note>>? = null


    private val _statusActivity = MutableLiveData<Event<Boolean>>()
    val statusActivity: LiveData<Event<Boolean>>
        get() = _statusActivity


    fun insertNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(note)
        }

    }

    fun deleteNote(note : Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
    }

    fun runActivity(){
        _statusActivity.postValue(Event(true))
    }

    suspend fun getNoteFromId(id: Int): Note {
        val note = viewModelScope.async(Dispatchers.IO) {
            noteRepository.getNoteFromId(id)
        }
        return note.await()
    }

    fun getNotesHadTagName(tagName: String) {
        noteHadTag = noteRepository.getNotesHadTagName(tagName)
    }

    fun getNotesInTime(dayOfMonth:Int , month :Int , year : Int):LiveData<List<Note>>{
        return noteRepository.getNotesInTime(dayOfMonth, month, year)
    }

    fun getMutableListNotesArrangeInTime(listNote : List<Note>) : List<Note>{
        var temp =  listNote.toMutableList()
            .sortedWith(compareBy<Note>{it.timeHour}.thenBy{it.timeMinute})
            .sortedBy { it.timeHour == null }
        return temp
    }


}