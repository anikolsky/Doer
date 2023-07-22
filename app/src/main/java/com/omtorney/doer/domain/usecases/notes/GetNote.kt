package com.omtorney.doer.domain.usecases.notes

import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.domain.repository.Repository
import javax.inject.Inject

class GetNote @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}
