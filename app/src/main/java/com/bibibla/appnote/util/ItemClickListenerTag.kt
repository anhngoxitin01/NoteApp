package com.bibibla.appnote.util

import com.bibibla.appnote.model.Tag

interface ItemClickListenerTag {
    fun onItemClickListener(tag : Tag)
    fun onItemLongClickListener(position: Int, tag: Tag)
}