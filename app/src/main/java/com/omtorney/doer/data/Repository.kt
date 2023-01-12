package com.omtorney.doer.data

import com.omtorney.doer.data.database.NoteDao
import com.omtorney.doer.model.Note
import javax.inject.Inject

class Repository @Inject constructor(
    private val noteDao: NoteDao
) {
    suspend fun addNote(note: Note) = noteDao.insert(note)

    suspend fun updateNote(note: Note) = noteDao.update(note)

    suspend fun deleteNote(note: Note) = noteDao.delete(note)
}