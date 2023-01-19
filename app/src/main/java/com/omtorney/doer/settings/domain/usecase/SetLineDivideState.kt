package com.omtorney.doer.settings.domain.usecase

import com.omtorney.doer.core.domain.Repository

class SetLineDivideState(
    private val repository: Repository
) {

    suspend operator fun invoke(state: Boolean) {
        repository.setLineSeparatorState(state)
    }
}