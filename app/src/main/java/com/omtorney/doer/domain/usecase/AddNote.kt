package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.InvalidNoteException
import com.omtorney.doer.model.Note

class AddNote(
    private val repository: Repository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.text.isBlank()) {
            throw InvalidNoteException("Note can't be empty")
        }
        repository.insertNote(note)
    }
}