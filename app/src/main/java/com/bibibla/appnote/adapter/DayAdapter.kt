package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemScheduleNoteBinding
import com.bibibla.appnote.diff.DayDiff
import com.bibibla.appnote.model.Note

class DayAdapter : ListAdapter<Note, DayAdapter.ViewHolder>(DayDiff()) {
    inner class ViewHolder(private val binding: ItemScheduleNoteBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(note : Note , position: Int){
            binding.tvIndexNote.text = (position + 1).toString()
            binding.tvTitleNote.text = note.title
            binding.tvDescriptionNote.text = note.description
            binding.tvTimeNote.text = note.timeHour.toString() + ":" + note.timeMinute.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayAdapter.ViewHolder {
        return ViewHolder(ItemScheduleNoteBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: DayAdapter.ViewHolder, position: Int) {
        val noteDay = getItem(position)
        noteDay?.let {
            holder.bind(it , position)
        }
    }
}