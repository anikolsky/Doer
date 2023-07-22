package com.omtorney.doer.data

import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.local.LocalDataSource
import com.omtorney.doer.data.remote.RemoteDataSource
import com.omtorney.doer.data.remote.database.FirestoreResult
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.note.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override fun getNotes(): Flow<List<Note>> = localDataSource.getNotes()
    override suspend fun getNoteById(id: Long): Note? = localDataSource.getNoteById(id)
    override suspend fun insertNote(note: Note) = localDataSource.insertNote(note)
    override suspend fun deleteNote(note: Note) = localDataSource.deleteNote(note)

    override fun getGoals(): Flow<List<Goal>> = localDataSource.getGoals()
    override suspend fun getGoalById(id: Long): Goal? = localDataSource.getGoalById(id)
    override suspend fun insertGoal(goal: Goal) = localDataSource.insertGoal(goal)
    override suspend fun deleteGoal(goal: Goal) = localDataSource.deleteGoal(goal)

    override val noteSeparatorState: Flow<Boolean> = localDataSource.noteSeparatorState
    override suspend fun setNoteSeparatorState(state: Boolean) = localDataSource.setNoteSeparatorState(state)
    override val noteSeparatorSize: Flow<Int> = localDataSource.noteSeparatorSize
    override suspend fun setNoteSeparatorSize(size: Int) = localDataSource.setNoteSeparatorSize(size)
    override val accentColor: Flow<Long> = localDataSource.accentColor
    override suspend fun setAccentColor(color: Long) = localDataSource.setAccentColor(color)
    override val secondaryColor: Flow<Long> = localDataSource.secondaryColor
    override suspend fun setSecondaryColor(color: Long) = localDataSource.setSecondaryColor(color)

    override fun getUsers(userName: String): Flow<FirestoreResult<List<FirestoreUser?>>> =
        remoteDataSource.getUsers(userName)

    override suspend fun createBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        remoteDataSource.createBackup(firestoreUser)

    override suspend fun updateBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        remoteDataSource.updateBackup(firestoreUser)

    override suspend fun deleteBackup(firestoreUser: FirestoreUser): Flow<FirestoreResult<String>> =
        remoteDataSource.deleteBackup(firestoreUser)
}
