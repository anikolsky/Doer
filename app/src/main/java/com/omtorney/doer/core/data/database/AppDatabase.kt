package com.omtorney.doer.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.goals.domain.model.GoalStepsConverter
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NotePriorityConverter

@Database(
    entities = [
        Note::class,
        Goal::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        NotePriorityConverter::class,
        GoalStepsConverter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun goalDao(): GoalDao
}