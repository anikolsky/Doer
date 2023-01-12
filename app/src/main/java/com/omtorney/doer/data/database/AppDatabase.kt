package com.omtorney.doer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omtorney.doer.model.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}