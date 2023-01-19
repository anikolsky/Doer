package com.omtorney.doer.core.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.model.Note

class DeleteNote(
    private val repository: Repository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}