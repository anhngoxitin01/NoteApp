package com.bibibla.appnote.diff

import androidx.recyclerview.widget.DiffUtil
import com.bibibla.appnote.model.Note

class DayDiff : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}