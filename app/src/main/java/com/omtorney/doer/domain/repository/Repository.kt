package com.omtorney.doer.domain.repository

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.data.remote.database.FirestoreResult
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.note.Note
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

    val noteSeparatorState: Flow<Boolean>
    suspend fun setNoteSeparatorState(state: Boolean)
    val noteSeparatorSize: Flow<Int>
    suspend fun setNoteSeparatorSize(size: Int)
    val accentColor: Flow<Long>
    suspend fun setAccentColor(color: Long)
    val secondaryColor: Flow<Long>
    suspend fun setSecondaryColor(color: Long)

    fun getUsers(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>> // TODO rename
    suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
}
