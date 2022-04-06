package com.bibibla.appnote.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
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
    private var titles = arrayListOf<String>("Mo" , "Tu", "We", "Th", "Fr", "Sa", "Su")

    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBackWeek.setOnClickListener{
            scheduleViewModel.addDayCalendar(-7)
            binding.tvTimeWeek.text = scheduleViewModel.getStringTimeWeek()
            updateUI()
        }

        binding.imgNextWeek.setOnClickListener{
            scheduleViewModel.addDayCalendar(7)
            binding.tvTimeWeek.text = scheduleViewModel.getStringTimeWeek()
            updateUI()
        }

        binding.tvTimeWeek.text = scheduleViewModel.getStringTimeWeek()
        updateUI()
    }

    private fun updateUI() {
        //pager adapter
        pagerAdapter = DayPagerAdapter(this)
        binding.vpDayNote.adapter = pagerAdapter

        // open this day
        var dayOfWeek = 0
        dayOfWeek = scheduleViewModel.calendar.get(Calendar.DAY_OF_WEEK)
        binding.vpDayNote.setCurrentItem(dayOfWeek - 2)

        //create tabLayout
        TabLayoutMediator(binding.tabWeek , binding.vpDayNote) {
                tab, position ->
            tab.text = titles[position] + "   " + scheduleViewModel.getCalender(position + 2).get(Calendar.DAY_OF_MONTH)
        }.attach()
    }
}