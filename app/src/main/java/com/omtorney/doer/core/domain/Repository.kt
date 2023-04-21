package com.omtorney.doer.core.domain

import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.firestore.data.FirestoreResult
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

    fun getUsers(): Flow<FirestoreResult<List<FirestoreUser?>>>
    suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
    suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>>
}