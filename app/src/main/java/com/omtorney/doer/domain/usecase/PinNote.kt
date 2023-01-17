package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note

class PinNote(private val repository: Repository) {

    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}