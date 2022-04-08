package com.bibibla.appnote.ui.fragment

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Application
import android.app.PendingIntent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.NoteAdapter
import com.bibibla.appnote.broadcast.NotificationReceiver
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
    val parentAcContext = context

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
            Log.d("check", "get arrNote: $it")
            adapter.submitList(it)
        })
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager =  GridLayoutManager(parentAcContext , 2 , GridLayoutManager.VERTICAL , false)



        return binding.root
    }

    override fun onItemClickListener(note: Note) {
        val intent = Intent( parentAcContext, NoteActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("note" , note)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onItemLongClickListener(position: Int, note: Note) {

        //create delete builder
        val deleteBuilder = AlertDialog.Builder(parentAcContext)
        deleteBuilder.apply {
            setTitle("Delete note")
            setMessage("Do you want to delete this Note!!")
            setPositiveButton("Yes",
                DialogInterface.OnClickListener{dialogInterface, id ->
                    noteViewModel.deleteNote(note)
                    tagViewModel.deleteTagByNote(note)
                    cancelNotification(note)
                })
            setNegativeButton("No",
                DialogInterface.OnClickListener{dialogInterface, id ->
                    dialogInterface.cancel()
                })
        }

        //create menu builder
        val menuBuilder = AlertDialog.Builder(parentAcContext)
        val menuAdapter = ArrayAdapter<String>(parentAcContext , R.layout.select_dialog_item ,MConst.MAIN_MENU)
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
                            Toast.makeText(parentAcContext ,"Not found select in dialog", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }

        val menuDialog = menuBuilder.create()
        menuDialog.show()
        Log.d("check" , "running dialog")
    }

    private fun cancelNotification(note: Note) {
        if(note.isSettingAlarm) {
            Log.d("check", "running cancel notification")
            Log.d("check", "note : ${note.toString()}")

            //create and send intent for broadcast
            val intent = Intent(parentAcContext, NotificationReceiver::class.java)
            intent.setAction(MConst.NOTIFICATION_CUSTOM)
            intent.putExtra("title", note.title.toString())
            intent.putExtra("description", note.description.toString())
            intent.putExtra("id", note.id)

            //create pending intent
            val pendingIntent = PendingIntent.getBroadcast(
                parentAcContext,
                note.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            // create alarm to notification in broadcast after time
            val am = parentAcContext.getSystemService(Application.ALARM_SERVICE) as AlarmManager
            //delete the notification
            am.cancel(pendingIntent)
        }
    }
}