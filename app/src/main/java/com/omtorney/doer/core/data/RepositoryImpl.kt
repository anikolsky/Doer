package com.omtorney.doer.core.data

import com.omtorney.doer.core.data.local.GoalDao
import com.omtorney.doer.core.data.local.NoteDao
import com.omtorney.doer.firestore.data.remote.FirestoreDatabase
import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.goals.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val goalDao: GoalDao,
    private val firestoreDatabase: FirestoreDatabase
) : Repository {

    override fun getNotes(): Flow<List<Note>> = noteDao.getNotes()
    override suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
    override suspend fun insertNote(note: Note) = noteDao.insert(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)

    override fun getGoals(): Flow<List<Goal>> = goalDao.getGoals()
    override suspend fun getGoalById(id: Long): Goal? = goalDao.getGoalById(id)
    override suspend fun insertGoal(goal: Goal) = goalDao.insert(goal)
    override suspend fun deleteGoal(goal: Goal) = goalDao.delete(goal)

    override fun getUsers(): Flow<FirestoreResult<List<FirestoreUser?>>> =
        firestoreDatabase.getUsers()

    override suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.createBackup(firestoreUser)

    override suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.updateBackup(firestoreUser)

    override suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        firestoreDatabase.deleteBackup(firestoreUser)
}
