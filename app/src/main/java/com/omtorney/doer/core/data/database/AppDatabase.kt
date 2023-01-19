package com.omtorney.doer.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.doer.core.model.Note
import com.omtorney.doer.core.model.NotePriorityConverter

@Database(entities = [Note::class], version = 1)
@TypeConverters(NotePriorityConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}