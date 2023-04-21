package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.Note
import javax.inject.Inject

class PinNote @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}