package com.bibibla.appnote.ui.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bibibla.appnote.adapter.NoteAdapter
import com.bibibla.appnote.databinding.FragmentTagNotesBinding
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.model.Tag
import com.bibibla.appnote.util.ItemClickListenerNote
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory


class TagNotesFragment(application: Application) : Fragment(), ItemClickListenerNote {
    private lateinit var binding: FragmentTagNotesBinding
//    val mContext = context

    private val noteViewModel : NoteViewModel by viewModels(){
        NoteViewModelFactory(application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTagNotesBinding.inflate(inflater , container , false)

        // lay tag tu tagActivity
        val bundle = arguments
        val tag: Tag = bundle?.get("tag") as Tag

        //create adapter
        val adapter = NoteAdapter(this)

        // create notes
        val arrNotes = noteViewModel.getNotesHadTag("%t%").value
        Log.d("check", "get arrNote" + arrNotes.toString())
        adapter.submitList(arrNotes)
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager =  GridLayoutManager(context , 2 , GridLayoutManager.VERTICAL , false)



        return binding.root
    }

    override fun onItemClickListener(note: Note) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClickListener(position: Int, note: Note) {
        TODO("Not yet implemented")
    }


}