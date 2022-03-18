package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemTkbNoteBinding
import com.bibibla.appnote.diff.DayDiff
import com.bibibla.appnote.model.Note

class MondayAdapter : ListAdapter<Note, MondayAdapter.ViewHolder>(DayDiff()) {
    inner class ViewHolder(private val binding: ItemTkbNoteBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(note : Note , position: Int){
            // TODO show notes here
            binding.tvIndexNote.text = (position + 1).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MondayAdapter.ViewHolder {
        return ViewHolder(ItemTkbNoteBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: MondayAdapter.ViewHolder, position: Int) {
        val noteDay = getItem(position)
        noteDay?.let {
            holder.bind(it , position)
        }
    }
}