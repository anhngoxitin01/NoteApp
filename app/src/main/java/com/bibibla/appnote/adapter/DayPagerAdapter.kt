package com.bibibla.appnote.adapter

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.bibibla.appnote.ui.fragment.*
import java.util.*
import android.R




class DayPagerAdapter (private val activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    val application = activity.application

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MondayFragment(application)
            1 -> TuesdayFragment(application)
            2 -> WednesdayFragment(application)
            3 -> ThursdayFragment(application)
            4 -> FridayFragment(application)
            5 -> SaturdayFragment(application)
            6 -> SundayFragment(application)
            else ->throw NotImplementedError("Unknown fragment for position : $position")
        }
    }


}