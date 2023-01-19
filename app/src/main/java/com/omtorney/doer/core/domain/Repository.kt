package com.omtorney.doer.core.domain

import com.omtorney.doer.core.model.Note
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    val getLineSeparatorState: Flow<Boolean>

    suspend fun setLineSeparatorState(enabled: Boolean)

    val getAccentColor: Flow<Long>

    suspend fun setAccentColor(color: Long)

    val getInitialColor: Long

    suspend fun setSecondaryColor(color: Long)

    val getSecondaryColor: Flow<Long>
}