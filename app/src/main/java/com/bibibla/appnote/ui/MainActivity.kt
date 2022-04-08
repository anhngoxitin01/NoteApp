package com.bibibla.appnote.ui

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.NoteAdapter
import com.bibibla.appnote.adapter.NotePinAdapter
import com.bibibla.appnote.broadcast.NotificationReceiver
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
    private val mNotificationReceiver : NotificationReceiver = NotificationReceiver()

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
        binding.fbtCreateNote.setOnClickListener {
            val intent = Intent(this , NoteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        //set nav listener
        binding.navMusic.setNavigationItemSelectedListener(this)

        //sending all intent to notification in broadcastReceiver
//        sendingIntentToBroadcastReceiver()

    }

    private fun sendingIntentToBroadcastReceiver() {
        Log.d("check" ,"sending broadcast")

        //get all value data
        noteViewModel.getNotesAlert(true)
        noteViewModel.noteAreAlarm?.observe(this ,{
            for (note in it)
            {
                val secondInMills = noteViewModel.caculTimeToNotification(note)
                Log.d("check" , secondInMills.toString())

                //not send to alarm if the alarm note is the pass
                if(secondInMills >= 0)
                {
                    Log.d("check" ,"note : ${note.toString()}")
                    //create and send intent for broadcast
                    val intent = Intent(this , NotificationReceiver::class.java)
                    intent.setAction(MConst.NOTIFICATION_CUSTOM)
                    intent.putExtra("title" , note.title.toString())
                    intent.putExtra("description" , note.description.toString())
                    intent.putExtra("id" , note.id)
//                sendBroadcast(intent)

                    val pendingIntent = PendingIntent.getBroadcast(this ,note.id , intent , PendingIntent.FLAG_UPDATE_CURRENT )


                    val am = getSystemService(ALARM_SERVICE) as AlarmManager

                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + secondInMills , pendingIntent)
//                am.cancel(pendingIntent)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val mNotificationIntentFilter = IntentFilter(MConst.NOTIFICATION_CUSTOM)
        registerReceiver(mNotificationReceiver , mNotificationIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mNotificationReceiver)
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
                    cancelNotification(note)
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

    private fun cancelNotification(note: Note) {
        if(note.isSettingAlarm)
        {
            Log.d("check" , "running cancel notification")
            Log.d("check" ,"note : ${note.toString()}")

            //create and send intent for broadcast
            val intent = Intent(this , NotificationReceiver::class.java)
            intent.setAction(MConst.NOTIFICATION_CUSTOM)
            intent.putExtra("title" , note.title.toString())
            intent.putExtra("description" , note.description.toString())
            intent.putExtra("id" , note.id)

            //create pending intent
            val pendingIntent = PendingIntent.getBroadcast(this ,note.id , intent , PendingIntent.FLAG_UPDATE_CURRENT )
            // create alarm to notification in broadcast after time
            val am = getSystemService(ALARM_SERVICE) as AlarmManager
            //delete the notification
            am.cancel(pendingIntent)
        }
    }


}