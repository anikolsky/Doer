package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.Note

class GetNote(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}