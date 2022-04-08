package com.bibibla.appnote.vm

import android.app.Application
import android.app.PendingIntent
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
import java.time.Duration
import java.util.*
import kotlin.math.log

class NoteViewModel(private val app : Application): ViewModel() {
    private val noteRepository = NoteRepository(
        NoteDatabase.getInstance(app.applicationContext , viewModelScope).getNoteDao()
    )

    val notes = noteRepository.getNotes()
    val notesPin = noteRepository.getNotesPin()
    /*must call get note first*/
    var noteAreAlarm : LiveData<List<Note>>? = null
    /*must call get note first*/
    var noteHadTag : LiveData<List<Note>>? = null


    private val _statusActivity = MutableLiveData<Event<Boolean>>()
    val statusActivity: LiveData<Event<Boolean>>
        get() = _statusActivity


    fun caculTimeToNotification(note : Note): Long {

        var secondInMills : Long = -1;

        val calendar = Calendar.getInstance()
        //add one month to calendar
        calendar.add(Calendar.MONTH, 1)
        val minute = calendar.get(Calendar.MINUTE)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        //cacul time
        val timeNote = Date(
            if(note.dateYear == null) year else note.dateYear!!,
            if(note.dateMonth == null) month else note.dateMonth!!,
            if(note.dateDay == null) day else note.dateDay!!,
            if(note.timeHour == null) hour else note.timeHour!!,
            if(note.timeMinute == null) minute else note.timeMinute!!
        )
        // get Date have time like calendar have
        val timeNow = Date(year , month , day , hour , minute)

        if(timeNote.after(timeNow))
        {
//            Log.d("check" , "the time of note is after calendar")
            secondInMills = timeNote.time - ( timeNow.time + calendar.get(Calendar.MILLISECOND))
        }

//        Log.d("check" , note.toString())
//        Log.d("check" , "detail timeNote: " +
//                "year = ${timeNote.year} " +
//                "month = ${timeNote.month} " +
//                "day = ${timeNote.date} " +
//                "hour = ${timeNote.hours} " +
//                "minute = ${timeNote.minutes} ")
//        Log.d("check" , "timeNote : ${timeNote.time} || timeNow : ${timeNow.time}")
//        Log.d("check" , "secondInMills : ${secondInMills}")

        return if(secondInMills < 0) -1 else secondInMills
    }

    fun getNotesAlert(isSettingAlarm:Boolean){
        noteAreAlarm = noteRepository.getNotesAlert(isSettingAlarm)
    }

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

    fun deleteDatabaseNote(){
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteDatabaseNote()
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

}