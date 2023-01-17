package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository

class SetLineDivideState(
    private val repository: Repository
) {

    suspend operator fun invoke(state: Boolean) {
        repository.setLineSeparatorState(state)
    }
}