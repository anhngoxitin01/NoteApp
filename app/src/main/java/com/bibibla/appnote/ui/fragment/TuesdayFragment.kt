package com.bibibla.appnote.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.DayAdapter
import com.bibibla.appnote.databinding.FragmentTuesdayBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.vm.ScheduleViewModel
import com.bibibla.appnote.vm.ScheduleViewModelFactory
import java.util.*

class TuesdayFragment(application: Application): Fragment(R.layout.fragment_tuesday) {

    private lateinit var binding: FragmentTuesdayBinding
    private lateinit var adapter: DayAdapter
    private val scheduleViewModel : ScheduleViewModel by viewModels(){
        ScheduleViewModelFactory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTuesdayBinding.inflate(inflater , container , false)

        //get date 
        val calendar = scheduleViewModel.getCalender(MConst.TUESDAY)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)



        // display here
        adapter = DayAdapter()
        scheduleViewModel.getNotesInTime(dayOfMonth, month, year).observe(viewLifecycleOwner ,{
            adapter.submitList(scheduleViewModel.getNotesArrangeInTime(it))
        })

        binding.rvMondayNoteItem.adapter = adapter
        binding.rvMondayNoteItem.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

        return binding.root
    }
}