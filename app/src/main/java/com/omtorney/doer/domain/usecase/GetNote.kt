package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note

class GetNote(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}