package com.omtorney.doer.domain.usecases.notes

import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.domain.repository.Repository
import javax.inject.Inject

class PinNote @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}
