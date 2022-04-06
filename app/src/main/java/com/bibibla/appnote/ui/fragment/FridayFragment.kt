package com.bibibla.appnote.ui.fragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.DayAdapter
import com.bibibla.appnote.databinding.FragmentFridayBinding
import com.bibibla.appnote.databinding.FragmentWednesdayBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.ui.NoteActivity
import com.bibibla.appnote.util.ItemClickListenerNote
import com.bibibla.appnote.vm.ScheduleViewModel
import com.bibibla.appnote.vm.ScheduleViewModelFactory
import java.util.*

class FridayFragment(application: Application ,private val activityContext: Context): Fragment(R.layout.fragment_friday) , ItemClickListenerNote {

    private lateinit var binding: FragmentFridayBinding
    private lateinit var adapter: DayAdapter
    private val scheduleViewModel : ScheduleViewModel by activityViewModels(){
        ScheduleViewModelFactory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFridayBinding.inflate(inflater , container , false)
        //get date
        val calendar = scheduleViewModel.getCalender(MConst.FRIDAY)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // display here
        adapter = DayAdapter(this)
        scheduleViewModel.getNotesInTime(dayOfMonth, month, year).observe(viewLifecycleOwner ,{
            if(it != null)
                adapter.submitList(scheduleViewModel.getNotesArrangeInTime(it))
            else
                adapter.submitList(null)
        })

        binding.rvMondayNoteItem.adapter = adapter
        binding.rvMondayNoteItem.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

        return binding.root
    }

    override fun onItemClickListener(note: Note) {
        val intent = Intent( activityContext , NoteActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("note" , note)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onItemLongClickListener(position: Int, note: Note) {
        TODO("Not yet implemented")
    }
}