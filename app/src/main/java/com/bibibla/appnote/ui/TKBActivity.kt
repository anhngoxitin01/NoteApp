package com.bibibla.appnote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bibibla.appnote.adapter.DayPagerAdapter
import com.bibibla.appnote.databinding.ActivityTkb2Binding
import com.google.android.material.tabs.TabLayoutMediator

class TKBActivity: AppCompatActivity() {
    private lateinit var adapter : DayPagerAdapter
    // must fix it
    private var titles = arrayListOf<String>("Thu 2" , "Thu 3", "Thu 4", "Thu 5", "Thu 6", "Thu 7", "Chu Nhat")

    private lateinit var binding: ActivityTkb2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTkb2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DayPagerAdapter(this)
        binding.vpDayNote.adapter = adapter

        TabLayoutMediator(binding.tabWeek , binding.vpDayNote) {
                tab, position ->
            tab.text = titles[position]
        }.attach()

    }
}