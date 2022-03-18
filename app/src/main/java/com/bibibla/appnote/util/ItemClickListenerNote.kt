package com.bibibla.appnote.util

import com.bibibla.appnote.model.Note

interface ItemClickListenerNote {
    fun onItemClickListener(note : Note)
    fun onItemLongClickListener(position: Int, note: Note)
}