package com.bibibla.appnote.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.bibibla.appnote.databinding.ActivityNoteBinding
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import com.bibibla.appnote.vm.TagViewModel
import com.bibibla.appnote.vm.TagViewModelFactory
import com.bibibla.appnote.broadcast.NotificationReceiver
import com.bibibla.appnote.model.MConst


class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    private val noteViewModel: NoteViewModel by viewModels() {
        NoteViewModelFactory(application)
    }
    private val tagViewModel : TagViewModel by viewModels(){
        TagViewModelFactory(application)
    }
    private lateinit var note: Note

    var oldTag: String? = null
    // this value to save the old value of note to check for cancel notification
    var oldStatusAlertNote : Boolean = false

    // 0 is create 1 is update
    var status: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check status Activity
        val intent = intent.getSerializableExtra("note")
        if (intent != null) {
            status = 1
            note = intent as Note
            binding.edtTitle.setText(note.title)
            binding.edtDescription.setText(note.description)
            binding.edtTag.setText(note.tags)
            oldTag = note.tags.toString()

            if(note.isSettingAlarm)
            {
                binding.swAlert.isChecked = true
                //update value for oldStatusAlertNote
                oldStatusAlertNote = true

                if (note.timeMinute != null) {
                    binding.swChooseTime.isChecked = true
                    binding.tpTimePicker.minute = note.timeMinute!!
                    binding.tpTimePicker.hour = note.timeHour!!
                }
                if (note.dateDay != null) {
                    binding.swChooseDay.isChecked = true
                    binding.tpDayPicker.updateDate(note.dateYear!!, note.dateMonth!!, note.dateDay!!)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            //get information from form
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            var timeMinute: Int? = null
            var timeHour: Int? = null
            var dateDay: Int? = null
            var dateMonth: Int? = null
            var dateYear: Int? = null
            val tags: String? = binding.edtTag.text.toString()
            val isSettingAlarm : Boolean = binding.swAlert.isChecked

            if (binding.swChooseTime.isChecked) {
                timeMinute = binding.tpTimePicker.minute
                timeHour = binding.tpTimePicker.hour
            }
            if (binding.swChooseDay.isChecked) {
                dateDay = binding.tpDayPicker.dayOfMonth
                dateMonth = binding.tpDayPicker.month + 1
                dateYear = binding.tpDayPicker.year
            }

            //create Tag
            if(tags != null && oldTag != null && oldTag!!.compareTo(tags) != 0)
            {
                Log.d("check", "running createTag")
                createAndUpdateTag(tags.split(",") , oldTag!!.split(","))
            } else if (tags != null && oldTag == null)
            {
                createTag(tags.split(","))
            }


            //update or create note
            when (status) {
                0 -> {  // create
                    val tempNote = Note(
                        title = title,
                        description = description,
                        timeMinute = timeMinute,
                        timeHour = timeHour,
                        dateDay = dateDay,
                        dateMonth = dateMonth,
                        dateYear = dateYear,
                        tags = tags,
                        isSettingAlarm = isSettingAlarm
                    )
                    //create note
                    noteViewModel.insertNote(tempNote)
                    Log.d("check", "create Note")
                    //update notification for note
                    updateNotificationNote(tempNote)
                }
                1 -> {  // update
                    note.title = title
                    note.description = description
                    note.timeMinute = timeMinute
                    note.timeHour = timeHour
                    note.dateDay = dateDay
                    note.dateMonth = dateMonth
                    note.dateYear = dateYear
                    note.tags = tags
                    note.isSettingAlarm = isSettingAlarm
                    //update note
                    noteViewModel.updateNote(note)
                    Log.d("check", "running update")
                    //check the swAlert to cancel if change to false
                    checkToCancelOrUpdateNotificationNote(note)
                }
                else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateNotificationNote(tempNote: Note) {
        Log.d("check" , "update notification")
        Log.d("check" ,"note : ${tempNote.toString()}")

        //get time top notification
        val secondInMills = noteViewModel.caculTimeToNotification(tempNote)
//        Log.d("check" , secondInMills.toString())

        //just notification for the note can notification
        if(secondInMills >= 0)
        {
            //create and send intent for broadcast
            val intent = Intent(this , NotificationReceiver::class.java)
            intent.setAction(MConst.NOTIFICATION_CUSTOM)
            intent.putExtra("title" , tempNote.title.toString())
            intent.putExtra("description" , tempNote.description.toString())
            intent.putExtra("id" , tempNote.id)

            //create pending intent
            val pendingIntent = PendingIntent.getBroadcast(this ,tempNote.id , intent , PendingIntent.FLAG_UPDATE_CURRENT )
            // create alarm to notification in broadcast after time
            val am = getSystemService(ALARM_SERVICE) as AlarmManager
            //update the notification
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + secondInMills , pendingIntent)
        }
    }

    private fun checkToCancelOrUpdateNotificationNote(note: Note) {
        Log.d("check" , "running check to cancel or update notification")

        //this note check in if is the note had from intent pass to this activity
        // not the note in parameter
        if(!binding.swAlert.isChecked && oldStatusAlertNote)
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
            val pendingIntent = PendingIntent.getBroadcast(this , note.id , intent , PendingIntent.FLAG_UPDATE_CURRENT )
            // create alarm to notification in broadcast after time
            val am = getSystemService(ALARM_SERVICE) as AlarmManager
            //delete the notification
            am.cancel(pendingIntent)
        } else {
            //if swAlert is turn on update the notification of note
            updateNotificationNote(note)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
           finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTag(newTags: List<String>) {
        tagViewModel.checkTagToAddFromList(newTags)
    }

    private fun createAndUpdateTag(newTags: List<String>, oldTags: List<String>) {

        // xoa phan tu da xoa
        tagViewModel.checkTagToDeleteOrUpdate(newTags, oldTags)

        // tao tag moi
        tagViewModel.checkTagToAddFromList(newTags)
    }

}