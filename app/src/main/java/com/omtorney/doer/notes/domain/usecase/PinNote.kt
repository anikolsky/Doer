package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.Note

class PinNote(private val repository: Repository) {

    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}