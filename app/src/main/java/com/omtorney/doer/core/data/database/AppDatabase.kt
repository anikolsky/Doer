package com.omtorney.doer.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.goals.domain.model.GoalStepConverters
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NoteConverters

@Database(
    entities = [
        Note::class,
        Goal::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        NoteConverters::class,
        GoalStepConverters::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun goalDao(): GoalDao
}