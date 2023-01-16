package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note

class DeleteNote(
    private val repository: Repository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}