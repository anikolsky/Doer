package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.InvalidNoteException
import com.omtorney.doer.notes.domain.model.Note
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: Repository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.content.isEmpty()) {
            throw InvalidNoteException("Note content can't be empty")
        }
        repository.insertNote(note)
    }
}