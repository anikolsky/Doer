package com.omtorney.doer.data.local

import com.omtorney.doer.data.local.database.GoalDao
import com.omtorney.doer.data.local.database.NoteDao
import com.omtorney.doer.data.local.datastore.SettingsStore
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.note.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val goalDao: GoalDao,
    private val settingsStore: SettingsStore
) : LocalDataSource {

    override fun getNotes(): Flow<List<Note>> = noteDao.getNotes()
    override suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
    override suspend fun insertNote(note: Note) = noteDao.upsert(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)

    override fun getGoals(): Flow<List<Goal>> = goalDao.getGoals()
    override suspend fun getGoalById(id: Long): Goal? = goalDao.getGoalById(id)
    override suspend fun insertGoal(goal: Goal) = goalDao.upsert(goal)
    override suspend fun deleteGoal(goal: Goal) = goalDao.delete(goal)

    override val noteSeparatorState: Flow<Boolean> = settingsStore.noteSeparatorState
    override suspend fun setNoteSeparatorState(state: Boolean) = settingsStore.setNoteSeparatorState(state)
    override val noteSeparatorSize: Flow<Int> = settingsStore.noteSeparatorSize
    override suspend fun setNoteSeparatorSize(size: Int) = settingsStore.setNoteSeparatorSize(size)
    override val accentColor: Flow<Long> = settingsStore.accentColor
    override suspend fun setAccentColor(color: Long) = settingsStore.setAccentColor(color)
    override val secondaryColor: Flow<Long> = settingsStore.secondaryColor
    override suspend fun setSecondaryColor(color: Long) = settingsStore.setSecondaryColor(color)
}
