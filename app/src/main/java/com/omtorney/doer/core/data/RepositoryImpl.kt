package com.omtorney.doer.core.data

import com.omtorney.doer.core.data.database.GoalDao
import com.omtorney.doer.core.data.database.NoteDao
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.goals.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val goalDao: GoalDao
) : Repository {

    override fun getNotes(): Flow<List<Note>> = noteDao.getNotes()
    override suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
    override suspend fun insertNote(note: Note) = noteDao.insert(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)

    override fun getGoals(): Flow<List<Goal>> = goalDao.getGoals()
    override suspend fun getGoalById(id: Long): Goal? = goalDao.getGoalById(id)
    override suspend fun insertGoal(goal: Goal) = goalDao.insert(goal)
    override suspend fun deleteGoal(goal: Goal) = goalDao.delete(goal)
}