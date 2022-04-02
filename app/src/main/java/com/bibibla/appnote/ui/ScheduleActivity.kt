package com.bibibla.appnote.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bibibla.appnote.adapter.DayPagerAdapter
import com.bibibla.appnote.databinding.ActivityScheduleBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class ScheduleActivity: AppCompatActivity() {
    private lateinit var adapter : DayPagerAdapter
    // must fix it
    private var titles = arrayListOf<String>("Thu 2" , "Thu 3", "Thu 4", "Thu 5", "Thu 6", "Thu 7", "Chu Nhat")

    private lateinit var binding: ActivityScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DayPagerAdapter(this)
        binding.vpDayNote.adapter = adapter

        //get day of today
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        Log.d("check1" , dayOfWeek.toString())
        // open this day
        binding.vpDayNote.setCurrentItem(dayOfWeek - 2)

        //create tabLayout
        TabLayoutMediator(binding.tabWeek , binding.vpDayNote) {
                tab, position ->
            tab.text = titles[position]
        }.attach()

    }
}