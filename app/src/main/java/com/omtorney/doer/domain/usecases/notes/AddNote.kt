package com.omtorney.doer.domain.usecases.notes

import com.omtorney.doer.data.model.note.InvalidNoteException
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.domain.repository.Repository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: Repository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.content.isEmpty()) {
            throw InvalidNoteException("NoteEdit content can't be empty")
        }
        repository.insertNote(note)
    }
}
