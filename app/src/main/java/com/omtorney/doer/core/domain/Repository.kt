package com.omtorney.doer.core.domain

import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)

    fun getGoals(): Flow<List<Goal>>
    suspend fun getGoalById(id: Long): Goal?
    suspend fun insertGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)

    val getLineSeparatorState: Flow<Boolean>
    suspend fun setLineSeparatorState(enabled: Boolean)
    val getAccentColor: Flow<Long>
    suspend fun setAccentColor(color: Long)
    val getSecondaryColor: Flow<Long>
    suspend fun setSecondaryColor(color: Long)
}