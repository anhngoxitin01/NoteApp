package com.bibibla.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bibibla.appnote.databinding.ItemTagBinding
import com.bibibla.appnote.diff.TagDiff
import com.bibibla.appnote.model.Tag
import com.bibibla.appnote.util.ItemClickListenerTag

class TagAdapter(private val itemClickListenerTag: ItemClickListenerTag) : ListAdapter<Tag, TagAdapter.ViewHolder>(TagDiff()), Filterable {
    var list = mutableListOf<Tag>()

    inner class ViewHolder(val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: Tag) {
            binding.tvNameTag.text = tag.name
            binding.tvNumberTag.text = tag.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = getItem(position)
        tag?.let {
            holder.bind(it)
        }
        holder.binding.root.setOnClickListener {
            itemClickListenerTag.onItemClickListener(tag)
        }
        holder.binding.root.setOnLongClickListener {
            itemClickListenerTag.onItemLongClickListener(position, tag)
            true
        }
    }

    override fun getFilter(): Filter {
        return mFilter;
    }

    fun setData(list: MutableList<Tag>?){
        this.list = list!!
        submitList(list)
    }

    val mFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            //tao danh sach rong
            val arrTag = mutableListOf<Tag>()

            //tim kiem va them vao danh sach hien thi
            if (p0 == null || p0.isEmpty()) {
                //TODO fix
                arrTag.addAll(list)
//                this@TagAdapter.submitList(list)

            } else {
                for (i in currentList)
                    if (i.name.contains(p0, false))
                        arrTag.add(i)
            }

            //khoi tao FilterResults de tra ve
            val filterResults = FilterResults()
            filterResults.values = arrTag
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(p1?.values as MutableList<Tag>?)
        }

    }

}