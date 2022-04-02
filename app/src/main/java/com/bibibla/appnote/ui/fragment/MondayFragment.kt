package com.bibibla.appnote.ui.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.DayAdapter
import com.bibibla.appnote.databinding.FragmentMondayBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.vm.ScheduleViewModel
import com.bibibla.appnote.vm.ScheduleViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class MondayFragment(application: Application): Fragment(R.layout.fragment_monday) {

    private lateinit var binding: FragmentMondayBinding
    private lateinit var adapter: DayAdapter
    private val scheduleViewModel : ScheduleViewModel by activityViewModels(){
        ScheduleViewModelFactory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMondayBinding.inflate(inflater , container , false)

        //get date
        val calendar = scheduleViewModel.getCalender(MConst.MONDAY)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // display here

        scheduleViewModel.getNotesInTime(dayOfMonth, month, year).observe(viewLifecycleOwner ,{
            adapter.submitList(scheduleViewModel.getNotesArrangeInTime(it))
        })

        binding.rvMondayNoteItem.adapter = adapter
        binding.rvMondayNoteItem.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)


        return binding.root
    }
}