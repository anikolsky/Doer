package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.InvalidNoteException
import com.omtorney.doer.notes.domain.model.Note

class AddNote(
    private val repository: Repository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.text.isEmpty()) {
            throw InvalidNoteException("Note can't be empty")
        }
        repository.insertNote(note)
    }
}