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
import android.content.Context


class DayPagerAdapter (private val activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    val application = activity.application
    val activityContext: Context = activity

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MondayFragment(application , activityContext)
            1 -> TuesdayFragment(application, activityContext)
            2 -> WednesdayFragment(application, activityContext)
            3 -> ThursdayFragment(application, activityContext)
            4 -> FridayFragment(application, activityContext)
            5 -> SaturdayFragment(application, activityContext)
            6 -> SundayFragment(application, activityContext)
            else ->throw NotImplementedError("Unknown fragment for position : $position")
        }
    }


}