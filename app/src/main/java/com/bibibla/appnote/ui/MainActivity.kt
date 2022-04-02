package com.bibibla.appnote.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.NoteAdapter
import com.bibibla.appnote.adapter.NotePinAdapter
import com.bibibla.appnote.databinding.ActivityMainBinding
import com.bibibla.appnote.model.MConst
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.util.ItemClickListenerNote
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import com.bibibla.appnote.vm.TagViewModel
import com.bibibla.appnote.vm.TagViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity :
    AppCompatActivity() ,
    NavigationView.OnNavigationItemSelectedListener,
    ItemClickListenerNote{
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterNote: NoteAdapter
    private lateinit var adapterNotePin: NotePinAdapter
    private val noteViewModel : NoteViewModel by viewModels(){
        NoteViewModelFactory(application)
    }

    private val tagViewModel : TagViewModel by viewModels(){
        TagViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterNote = NoteAdapter(this)
        noteViewModel.notes.observe(this, {
            adapterNote.submitList(it.reversed())
        })
        binding.rvNotes.adapter = adapterNote
        binding.rvNotes.layoutManager = GridLayoutManager(this , 2 , GridLayoutManager.VERTICAL , false)


        adapterNotePin = NotePinAdapter(this)
        noteViewModel.notesPin.observe(this , {
            adapterNotePin.submitList(it)
        })
        binding.rvPinNotes.adapter = adapterNotePin
        binding.rvPinNotes.layoutManager = GridLayoutManager(this , 2 , GridLayoutManager.VERTICAL , false)

        //create menu
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        //create new note
        binding.btnCreateNote.setOnClickListener {
            val intent = Intent(this , NoteActivity::class.java)
            startActivity(intent)
        }

        //set nav listener
        binding.navMusic.setNavigationItemSelectedListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            if (binding.dlNote.isDrawerOpen(GravityCompat.START)){
                binding.dlNote.closeDrawer(GravityCompat.START)
                return true
            } else {
                binding.dlNote.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.dlNote.closeDrawer(GravityCompat.START)
        return when(item.itemId){
            R.id.menu_schedule -> {
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_tag -> {
                val intent = Intent(this, TagActivity::class.java)
                startActivity(intent)
                true
            }

            else -> true
        }
    }

    override fun onItemClickListener(note: Note) {
        val intent = Intent(this , NoteActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("note" , note)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun onItemLongClickListener(position: Int, note: Note) {

        //create delete builder
        val deleteBuilder = AlertDialog.Builder(this)
        deleteBuilder.apply {
            setTitle("Delete note")
            setMessage("Do you want to delete this Note!!")
            setPositiveButton("Yes",
                DialogInterface.OnClickListener{dialogInterface, id ->
                    noteViewModel.deleteNote(note)
                    tagViewModel.deleteTagByNote(note)
                })
            setNegativeButton("No",
                DialogInterface.OnClickListener{dialogInterface, id ->
                    dialogInterface.cancel()
                })
        }

        //create menu builder
        val menuBuilder = AlertDialog.Builder(this)
        val menuAdapter = ArrayAdapter<String>(this , R.layout.select_dialog_item ,MConst.MAIN_MENU)
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
                            Toast.makeText(this@MainActivity ,"Not found select in dialog", Toast.LENGTH_LONG).show()
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