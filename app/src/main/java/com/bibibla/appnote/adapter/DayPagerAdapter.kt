package com.bibibla.appnote.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bibibla.appnote.ui.fragment.MondayFragment

class DayPagerAdapter (private val activity: AppCompatActivity) : FragmentStateAdapter(activity){
    val application = activity.application

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MondayFragment(application)
            1 -> MondayFragment(application)
            2 -> MondayFragment(application)
            3 -> MondayFragment(application)
            4 -> MondayFragment(application)
            5 -> MondayFragment(application)
            6 -> MondayFragment(application)
            else ->throw NotImplementedError("Unknown fragment for position : $position")
        }
    }
}