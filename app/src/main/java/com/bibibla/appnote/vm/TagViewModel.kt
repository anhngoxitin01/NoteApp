package com.bibibla.appnote.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bibibla.appnote.db.TagDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.model.Tag
import com.bibibla.appnote.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TagViewModel(private val app : Application): ViewModel() {

    private val tagRepository = TagRepository(
        TagDatabase.getInstance(app.applicationContext , viewModelScope).getTagDao()
    )

    val tags = tagRepository.getTags()


    fun addTag(tag : Tag){
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.insertTag(tag)
        }
    }

    fun deleteTag(tag : Tag){
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.deleteTag(tag)
        }
    }

    fun deleteDatabaseTag(){
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.deleteDatabaseTag()
        }
    }

    fun updateOrDeleteTag(tag : Tag){
        viewModelScope.launch(Dispatchers.IO) {
            if (tag.amount == 0)
                tagRepository.deleteTag(tag)
            tagRepository.updateTag(tag)
        }
    }

    suspend fun getTagFromName(name: String): Tag{
        val tag = viewModelScope.async(Dispatchers.IO) {
            tagRepository.getTagsFromName(name)
        }
        return tag.await()
    }

    fun checkTagToDeleteOrUpdate(newTags: List<String>, oldTags: List<String>){
        if (oldTags.size > 0)
        {
            for(i in oldTags){
                if (i == "")
                    continue
                //khoi tao bien de check
                var isSame = false
                for ( j in newTags){
                    if (i.compareTo(j) == 0)
                    {
                        isSame = true
                        break
                    }
                }
                if (!isSame)
                {
                    //xoa tag (giam so luong tag dang dc danh dau)
                    viewModelScope.launch(Dispatchers.IO) {
                        Log.d("check", "check tag in checkTagToDeleteOrUpdate tag: " + i.trim())
                        var tag = tagRepository.getTagsFromName(i.trim())
                        Log.d("check", "check tag in checkTagToDeleteOrUpdate tag: $tag")
                        tag.amount -= 1
                        updateOrDeleteTag(tag)
                    }
                }
            }
        }
    }

    fun checkTagToAddFromList(newTags: List<String>, oldTags: List<String>){
        for( i in newTags){
            // tao bien temp vi i val
            val tempNameTag = i.trim()
            if (tempNameTag == "")
                continue
            Log.d("check", "check the tag : $i to create or increase amount")
            // Kiem tra tung phan tu cua new tag so vs old tag
            // nếu trùng thì bỏ qua kiểm tra phàn tử tiếp theo trong tag mới
            // nếu khác thì +amount hoặc tạo tag mới
            var isSame = false

            for(j in oldTags)
            {
                if (j == "")
                    continue
                if(i.trim().compareTo(j.trim()) == 0)
                {
                    isSame = true
                    break;
                }
            }

            if(!isSame)
            {
                viewModelScope.launch(Dispatchers.IO) {
                    var tag = getTagFromName(tempNameTag)
                    Log.d("check" , "tag value to update: ${tag.toString()}")
                    if (tag == null)
                    {
                        Log.d("check", "Create Tag in checkTagToAddFromList")
                        addTag(Tag(name = tempNameTag))
                    } else{
//                            Log.d("check", "plus tag : $tag amount")
                        tag.amount++
                        updateOrDeleteTag(tag)
                    }
                }
            }

        }
    }

    fun deleteTagByNote(note : Note){
        if (note.tags != "" || note.tags != null)
        {
            var arrTag = note.tags!!.split(",")
            for(i in arrTag){
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("check", "check tag in deleteTagByNote tag: " + i.trim())
                    var tag = tagRepository.getTagsFromName(i.trim())
                    if(tag != null){
                        Log.d("check", "check tag in deleteTagByNote tag: $tag")
                        tag.amount -= 1
                        updateOrDeleteTag(tag)
                    }
                }
            }
        }


    }

}