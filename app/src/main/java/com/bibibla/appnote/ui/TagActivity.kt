package com.bibibla.appnote.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bibibla.appnote.R
import com.bibibla.appnote.adapter.TagAdapter
import com.bibibla.appnote.databinding.ActivityTagBinding
import com.bibibla.appnote.model.Tag
import com.bibibla.appnote.ui.fragment.TagNotesFragment
import com.bibibla.appnote.util.ItemClickListenerTag
import com.bibibla.appnote.vm.NoteViewModel
import com.bibibla.appnote.vm.NoteViewModelFactory
import com.bibibla.appnote.vm.TagViewModel
import com.bibibla.appnote.vm.TagViewModelFactory

class TagActivity : AppCompatActivity() , ItemClickListenerTag {
    private lateinit var binding: ActivityTagBinding
    private lateinit var tagAdapter: TagAdapter


    private val tagViewModel : TagViewModel by viewModels(){
        TagViewModelFactory(application)
    }

    private val noteViewModel : NoteViewModel by viewModels(){
        NoteViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTagBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tagAdapter = TagAdapter(this)
        //set up for adapter
        tagViewModel.tags.observe(this , {
            tagAdapter.setData(it.toMutableList())
            Log.d("check", "tags size: " + it.size.toString())
        })
        binding.rvListTag.adapter = tagAdapter
        binding.rvListTag.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tag, menu)

        val search = menu?.findItem(R.id.nav_tag_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search something"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(textFind: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                // update recycle view here
                tagAdapter.filter.filter(p0)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClickListener(tag: Tag) {
        //create bundel
        val bundle = Bundle()
        bundle.putSerializable("tag" , tag)
        //set invisible for list tag
        binding.rvListTag.visibility = View.INVISIBLE
        //create fragment
        val fragment = TagNotesFragment(application)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view_tag, fragment).commit()

    }

    override fun onItemLongClickListener(position: Int, tag: Tag) {
        TODO("Not yet implemented")
    }
}