package com.bibibla.appnote.repository

import androidx.lifecycle.LiveData
import com.bibibla.appnote.db.TagDao
import com.bibibla.appnote.model.Tag

class TagRepository (private val tagDao : TagDao){
    suspend fun insertTag(tag: Tag){
        tagDao.insertTag(tag)
    }

    suspend fun updateTag(tag: Tag){
        tagDao.updateTag(tag)
    }

    suspend fun deleteTag(tag: Tag){
        tagDao.deleteTag(tag)
    }

    suspend fun deleteDatabaseTag(){
        tagDao.deleteDatabaseTag()
    }

    suspend fun getTagFromId(id: Int): Tag {
        return tagDao.getTagFromId(id)
    }

    suspend fun getTagsFromName(name: String): Tag {
        return tagDao.getTagFromName(name)
    }

    fun getTags(): LiveData<List<Tag>> {
        return tagDao.getAllTags()
    }
}