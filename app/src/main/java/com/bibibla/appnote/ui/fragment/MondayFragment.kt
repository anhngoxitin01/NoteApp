package com.bibibla.appnote.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.MondayAdapter
import com.bibibla.appnote.databinding.FragmentMondayBinding
import com.bibibla.appnote.manager.NoteManager

class MondayFragment: Fragment(R.layout.fragment_monday) {

    private lateinit var binding: FragmentMondayBinding
    private lateinit var adapter: MondayAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMondayBinding.inflate(inflater , container , false)
        // display here
        adapter = MondayAdapter()
        adapter.submitList(NoteManager.noteArr)
        binding.rvMondayNoteItem.adapter = adapter
        binding.rvMondayNoteItem.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        return binding.root
    }
}