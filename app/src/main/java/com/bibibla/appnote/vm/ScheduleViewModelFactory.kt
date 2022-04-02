package com.bibibla.appnote.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ScheduleViewModelFactory(private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)){
            return ScheduleViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown note view model")
    }

}