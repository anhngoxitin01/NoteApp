package com.bibibla.appnote.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bibibla.appnote.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class] , version = 5 , exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract  fun getNoteDao() : NoteDao

    companion object{
        private  var INSTANCE: NoteDatabase?= null

        @Synchronized
        fun getInstance(context: Context, viewModelScope: CoroutineScope) : NoteDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note.db"
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