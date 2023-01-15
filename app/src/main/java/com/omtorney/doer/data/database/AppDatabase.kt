package com.omtorney.doer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.doer.model.LocalDateTimeConverter
import com.omtorney.doer.model.Note
import com.omtorney.doer.model.NotePriorityConverter

@Database(entities = [Note::class], version = 1)
@TypeConverters(
    NotePriorityConverter::class,
    LocalDateTimeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}