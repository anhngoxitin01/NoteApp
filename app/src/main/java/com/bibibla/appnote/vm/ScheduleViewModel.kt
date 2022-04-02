package com.bibibla.appnote.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.db.NoteDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.repository.NoteRepository
import java.util.*

class ScheduleViewModel(private val app : Application): ViewModel() {
    private val noteRepository = NoteRepository(
        NoteDatabase.getInstance(app.applicationContext , viewModelScope).getNoteDao()
    )

    fun getNotesInTime(dayOfMonth:Int , month :Int , year : Int): LiveData<List<Note>> {
        return noteRepository.getNotesInTime(dayOfMonth, month, year)
    }

    fun getNotesArrangeInTime(listNote : List<Note>) : List<Note>{
        val temp =  listNote.toMutableList()
            .sortedWith(compareBy<Note>{it.timeHour}.thenBy{it.timeMinute})
            .sortedBy { it.timeHour == null }
        return temp
    }

    fun getCalender(dayOfWeek : Int):Calendar {
        var calender = Calendar.getInstance()
        val realDayOfWeek = calender.get(Calendar.DAY_OF_WEEK)
        if(realDayOfWeek < dayOfWeek)
            calender = getDayTomorrow( dayOfWeek - realDayOfWeek)
        else if(realDayOfWeek > dayOfWeek)
            calender = getDaysAgo( dayOfWeek - realDayOfWeek)
        return calender
    }

    private fun getDaysAgo(daysAgo: Int):Calendar {
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_YEAR, daysAgo)

        return  calender
    }

    private fun getDayTomorrow(daysTomorrow : Int):Calendar{
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_YEAR, daysTomorrow)

        return  calender
    }
}