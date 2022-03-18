package com.bibibla.appnote.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bibibla.appnote.ui.fragment.MondayFragment

class DayPagerAdapter (private val activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MondayFragment()
            1 -> MondayFragment()
            2 -> MondayFragment()
            3 -> MondayFragment()
            4 -> MondayFragment()
            5 -> MondayFragment()
            6 -> MondayFragment()
            else ->throw NotImplementedError("Unknown fragment for position : $position")
        }
    }
}