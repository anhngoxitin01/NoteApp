package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemNoteBinding
import com.bibibla.appnote.diff.NoteDiff
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.util.ItemClickListenerNote
import com.bibibla.appnote.vm.NoteViewModel

class NoteAdapter(private val itemClickListenerNote: ItemClickListenerNote)
    : ListAdapter<Note, NoteAdapter.ViewHolder>(NoteDiff()){

    inner class ViewHolder(val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(note : Note){
                binding.note = note
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
        holder.binding.root.setOnClickListener{
            itemClickListenerNote.onItemClickListener(note)
        }
        holder.binding.root.setOnLongClickListener {
            itemClickListenerNote.onItemLongClickListener(position,note)
            true
        }
    }

}