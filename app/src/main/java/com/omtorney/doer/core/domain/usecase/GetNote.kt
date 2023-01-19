package com.omtorney.doer.core.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.core.model.Note

class GetNote(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}