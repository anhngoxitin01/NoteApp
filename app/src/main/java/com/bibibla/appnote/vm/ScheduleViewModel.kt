package com.bibibla.appnote.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.db.NoteDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.repository.NoteRepository

class ScheduleViewModel(private val app : Application): ViewModel() {
    private val noteRepository = NoteRepository(
        NoteDatabase.getInstance(app.applicationContext , viewModelScope).getNoteDao()
    )

    fun getNotesInTime(dayOfMonth:Int , month :Int , year : Int): LiveData<List<Note>> {
        return noteRepository.getNotesInTime(dayOfMonth, month, year)
    }

    fun getNotesArrangeInTime(listNote : List<Note>) : List<Note>{
        var temp =  listNote.toMutableList()
            .sortedWith(compareBy<Note>{it.timeHour}.thenBy{it.timeMinute})
            .sortedBy { it.timeHour == null }
        return temp
    }
}