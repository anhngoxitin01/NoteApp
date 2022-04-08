package com.bibibla.appnote.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bibibla.appnote.model.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTag(tag: Tag)

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("DELETE FROM tag")
    suspend fun deleteDatabaseTag()

    @Query("SELECT * FROM tag")
    fun getAllTags() : LiveData<List<Tag>>

    @Query("SELECT * FROM tag WHERE _id = :id")
    suspend fun getTagFromId(id : Int): Tag

    @Query("SELECT * FROM tag WHERE _name = :name")
    suspend fun getTagFromName(name : String): Tag
}