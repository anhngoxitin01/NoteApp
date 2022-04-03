package com.bibibla.appnote.vm

import android.app.Application
import android.util.Log
import androidx.compose.ui.window.isPopupLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.db.NoteDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ScheduleViewModel(private val app : Application): ViewModel() {
    private val noteRepository = NoteRepository(
        NoteDatabase.getInstance(app.applicationContext , viewModelScope).getNoteDao()
    )

    var calendar = Calendar.getInstance()


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
        val realDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (realDayOfWeek < dayOfWeek)
            return getCalendarAfterDays(dayOfWeek - realDayOfWeek)
        else if (realDayOfWeek > dayOfWeek)
            return getCalendarAfterDays(dayOfWeek - realDayOfWeek)
        return calendar
    }

    private fun getCalendarAfterDays(daysAgo: Int):Calendar {
        val tempCalendar = this.calendar.clone() as Calendar
        tempCalendar.add(Calendar.DAY_OF_YEAR, daysAgo)
        return  tempCalendar
    }


    fun addDayCalendar(days :Int){
        calendar.add(Calendar.DAY_OF_YEAR , days)
    }
}