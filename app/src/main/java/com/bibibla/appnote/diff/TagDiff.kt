package com.bibibla.appnote.diff

import androidx.recyclerview.widget.DiffUtil
import com.bibibla.appnote.model.Tag

class TagDiff : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }
}