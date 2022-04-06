package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemScheduleNoteBinding
import com.bibibla.appnote.diff.DayDiff
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.util.ItemClickListenerNote

class DayAdapter(private val itemClickListenerNote: ItemClickListenerNote) : ListAdapter<Note, DayAdapter.ViewHolder>(DayDiff()) {
    inner class ViewHolder(val binding: ItemScheduleNoteBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(note : Note , position: Int){
            binding.note = note
            binding.index = (position + 1)
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
        holder.binding.root.setOnClickListener{
            itemClickListenerNote.onItemClickListener(noteDay)
        }
        holder.binding.root.setOnLongClickListener {
            itemClickListenerNote.onItemLongClickListener(position,noteDay)
            true
        }
    }
}