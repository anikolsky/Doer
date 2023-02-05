package com.omtorney.doer.core.data

import com.omtorney.doer.core.data.database.NoteDao
import com.omtorney.doer.settings.data.SettingsStore
import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.model.Note
import com.omtorney.doer.core.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val settingsStore: SettingsStore
) : Repository {

    override fun getNotes(): Flow<List<Note>> = noteDao.getNotes()

    override suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)

    override suspend fun insertNote(note: Note) = noteDao.insert(note)

    override suspend fun deleteNote(note: Note) = noteDao.delete(note)

    override val getLineSeparatorState = settingsStore.getLineSeparatorState

    override suspend fun setLineSeparatorState(enabled: Boolean) =
        settingsStore.setLineSeparatorState(enabled)

    override val getAccentColor: Flow<Long> = settingsStore.getAccentColor

    override suspend fun setAccentColor(color: Long) = settingsStore.setAccentColor(color)

    override val getInitialColor = Constants.INITIAL_COLOR

    override suspend fun setSecondaryColor(color: Long) = settingsStore.setSecondaryColor(color)

    override val getSecondaryColor: Flow<Long> = settingsStore.getSecondaryColor
}