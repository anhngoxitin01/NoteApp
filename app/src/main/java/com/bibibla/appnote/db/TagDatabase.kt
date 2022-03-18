package com.bibibla.appnote.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bibibla.appnote.model.Note
import com.bibibla.appnote.model.Tag
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Tag::class] , version = 2 , exportSchema = false)
abstract class TagDatabase: RoomDatabase() {
    abstract  fun getTagDao() : TagDao

    companion object{
        private  var INSTANCE: TagDatabase?= null

        @Synchronized
        fun getInstance(context: Context, viewModelScope: CoroutineScope) : TagDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TagDatabase::class.java,
                    "tag.db"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    })
                    .build()
            }
            return INSTANCE!!
        }
    }
}