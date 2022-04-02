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
import com.bibibla.appnote.adapter.MondayAdapter
import com.bibibla.appnote.databinding.FragmentMondayBinding
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import java.util.*

class MondayFragment(application: Application): Fragment(R.layout.fragment_monday) {

    private lateinit var binding: FragmentMondayBinding
    private lateinit var adapter: MondayAdapter
    private val noteViewModel : NoteViewModel by viewModels(){
        NoteViewModelFactory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMondayBinding.inflate(inflater , container , false)

        //get date 
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // display here
        adapter = MondayAdapter()
        noteViewModel.getNotesInTime(dayOfMonth, month, year).observe(viewLifecycleOwner ,{
            adapter.submitList(noteViewModel.getMutableListNotesArrangeInTime(it))
        })

        binding.rvMondayNoteItem.adapter = adapter
        binding.rvMondayNoteItem.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

        return binding.root
    }
}