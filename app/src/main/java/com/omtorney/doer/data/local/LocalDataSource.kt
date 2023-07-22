package com.omtorney.doer.data.local

import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.note.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)

    fun getGoals(): Flow<List<Goal>>
    suspend fun getGoalById(id: Long): Goal?
    suspend fun insertGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)

    val noteSeparatorState: Flow<Boolean>
    suspend fun setNoteSeparatorState(state: Boolean)
    val noteSeparatorSize: Flow<Int>
    suspend fun setNoteSeparatorSize(size: Int)
    val accentColor: Flow<Long>
    suspend fun setAccentColor(color: Long)
    val secondaryColor: Flow<Long>
    suspend fun setSecondaryColor(color: Long)
}
