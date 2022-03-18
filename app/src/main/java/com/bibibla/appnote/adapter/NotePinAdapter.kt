package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemPinNoteBinding
import com.bibibla.appnote.diff.NoteDiff
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.util.ItemClickListenerNote

class NotePinAdapter(private val itemClickListenerNote: ItemClickListenerNote)
    : ListAdapter<Note, NotePinAdapter.ViewHolder>(NoteDiff()){


    inner class ViewHolder(val binding: ItemPinNoteBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun bind(note : Note){
                binding.tvTitle.text = note.title
                binding.tvDescription.text = note.description
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPinNoteBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
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