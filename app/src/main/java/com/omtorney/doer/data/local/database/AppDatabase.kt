package com.omtorney.doer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.goal.GoalConverter
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.model.note.NoteConverter

@Database(
    entities = [
        Note::class,
        Goal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        NoteConverter::class,
        GoalConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun goalDao(): GoalDao
}
