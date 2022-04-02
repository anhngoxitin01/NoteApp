package com.bibibla.appnote.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.DayAdapter
import com.bibibla.appnote.adapter.DayPagerAdapter
import com.bibibla.appnote.databinding.ActivityScheduleBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.ui.fragment.MondayFragment
import com.bibibla.appnote.vm.ScheduleViewModel
import com.bibibla.appnote.vm.ScheduleViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ScheduleActivity: AppCompatActivity() {
    private lateinit var pagerAdapter : DayPagerAdapter
    private val scheduleViewModel : ScheduleViewModel by viewModels() {
        ScheduleViewModelFactory(application)
    }

    // must fix it
    private var titles = arrayListOf<String>("Thu 2" , "Thu 3", "Thu 4", "Thu 5", "Thu 6", "Thu 7", "Chu Nhat")

    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = DayPagerAdapter(this)
        binding.vpDayNote.adapter = pagerAdapter

        var dayOfWeek = 0
        dayOfWeek = scheduleViewModel.calendar.get(Calendar.DAY_OF_WEEK)

        // open this day
        binding.vpDayNote.setCurrentItem(dayOfWeek - 2)

        val count = 5

        binding.imgBackWeek.setOnClickListener{
            scheduleViewModel.addDayCalendar(-7)
            binding.tvTimeWeek.text = getStringTimeWeek()
        }

        binding.imgNextWeek.setOnClickListener{
            scheduleViewModel.addDayCalendar(7)
            binding.tvTimeWeek.text = getStringTimeWeek()
        }

        binding.tvTimeWeek.text = getStringTimeWeek()

        //create tabLayout
        TabLayoutMediator(binding.tabWeek , binding.vpDayNote) {
                tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun getStringTimeWeek():String {

        val tempCalendar : Calendar = scheduleViewModel.calendar.clone() as Calendar
        var tempStringTimeWeek = ""
        val dayOfWeek = scheduleViewModel.calendar.get(Calendar.DAY_OF_WEEK)


        tempCalendar.add(Calendar.DAY_OF_YEAR, MConst.MONDAY - dayOfWeek )

        tempStringTimeWeek += tempCalendar.get(Calendar.DAY_OF_MONTH)
        tempStringTimeWeek += "/"
        tempStringTimeWeek += tempCalendar.get(Calendar.MONTH) + 1
        tempStringTimeWeek += "    -    "
        tempCalendar.add(Calendar.DAY_OF_YEAR , 6 )
        tempStringTimeWeek += tempCalendar.get(Calendar.DAY_OF_MONTH)
        tempStringTimeWeek += "/"
        tempStringTimeWeek += tempCalendar.get(Calendar.MONTH) + 1

        tempCalendar.add(Calendar.DAY_OF_YEAR, MConst.SUNDAY - dayOfWeek )

        return tempStringTimeWeek
    }
}