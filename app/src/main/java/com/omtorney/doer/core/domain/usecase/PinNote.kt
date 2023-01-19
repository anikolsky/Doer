package com.omtorney.doer.core.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.model.Note

class PinNote(private val repository: Repository) {

    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}