package com.bibibla.appnote.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bibibla.appnote.databinding.ActivityNoteBinding
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import com.bibibla.appnote.vm.TagViewModel
import com.bibibla.appnote.vm.TagViewModelFactory

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

//        binding.edtDescription.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(editable: Editable?) {
//
//            }
//
//        })

        binding.btnSave.setOnClickListener {
            //get information from form
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            var timeMinute: Int? = null
            var timeHour: Int? = null
            var dateDay: Int? = null
            var dateMonth: Int? = null
            var dateYear: Int? = null
            var tags: String? = binding.edtTag.text.toString()

            if (binding.swChooseTime.isChecked) {
                timeMinute = binding.tpTimePicker.minute
                timeHour = binding.tpTimePicker.hour
            }
            if (binding.swChooseDay.isChecked) {
                dateDay = binding.tpDayPicker.dayOfMonth
                dateMonth = binding.tpDayPicker.month
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
                        tags = tags
                    )
                    //create note
                    noteViewModel.insertNote(tempNote)
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
                    //update note
                    noteViewModel.updateNote(note)
                    Log.d("check", "running update")
                }
                else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

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