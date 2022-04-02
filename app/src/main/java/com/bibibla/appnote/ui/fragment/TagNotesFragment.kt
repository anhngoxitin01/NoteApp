package com.bibibla.appnote.ui.fragment

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.NoteAdapter
import com.bibibla.appnote.databinding.FragmentTagNotesBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.model.Tag
import com.bibibla.appnote.ui.NoteActivity
import com.bibibla.appnote.util.ItemClickListenerNote
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import com.bibibla.appnote.vm.TagViewModel
import com.bibibla.appnote.vm.TagViewModelFactory
import java.util.*


class TagNotesFragment(application: Application, context: Context) : Fragment(), ItemClickListenerNote {
    private lateinit var binding: FragmentTagNotesBinding
    val mContext = context

    private val noteViewModel : NoteViewModel by viewModels(){
        NoteViewModelFactory(application)
    }

    private val tagViewModel : TagViewModel by viewModels(){
        TagViewModelFactory(application)
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
        noteViewModel.getNotesHadTagName("%" + tag.name + "%")
        noteViewModel.noteHadTag?.observe(viewLifecycleOwner ,{
            Log.d("check", "get arrNote: " + it.toString())
            adapter.submitList(it)
        })
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager =  GridLayoutManager(mContext , 2 , GridLayoutManager.VERTICAL , false)

        return binding.root
    }

    override fun onItemClickListener(note: Note) {
        val intent = Intent( mContext, NoteActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("note" , note)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onItemLongClickListener(position: Int, note: Note) {
        //create delete builder
        val deleteBuilder = AlertDialog.Builder(mContext)
        deleteBuilder.apply {
            setTitle("Delete note")
            setMessage("Do you want to delete this Note!!")
            setPositiveButton("Yes",
                DialogInterface.OnClickListener{ dialogInterface, id ->
                    noteViewModel.deleteNote(note)
                    tagViewModel.deleteTagByNote(note)
                })
            setNegativeButton("No",
                DialogInterface.OnClickListener{ dialogInterface, id ->
                    dialogInterface.cancel()
                })
        }

        //create menu builder
        val menuBuilder = AlertDialog.Builder(mContext)
        val menuAdapter = ArrayAdapter<String>(mContext , R.layout.select_dialog_item , MConst.TAG_MENU)
        menuBuilder.apply {
            setAdapter(menuAdapter, object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when(p1){
                        // Pin
                        0 -> {
                            note.isPin = if(note.isPin == 0) 1 else 0
                            noteViewModel.updateNote(note)
                            Log.d("check" , "running dialog Pin")
                        }
                        //Delete
                        1 -> {
                            Log.d("check" , "running dialog delete")
                            val dialog = deleteBuilder.create()
                            dialog.show()
                        }
                        else -> {
                            Toast.makeText(mContext ,"Not found select in dialog", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }

        val menuDialog = menuBuilder.create()
        menuDialog.show()
        Log.d("check" , "running dialog")
    }


}