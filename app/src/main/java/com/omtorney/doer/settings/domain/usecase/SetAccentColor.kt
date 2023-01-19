package com.omtorney.doer.settings.domain.usecase

import com.omtorney.doer.core.domain.Repository

class SetAccentColor(
    private val repository: Repository
) {

    suspend operator fun invoke(color: Long) {
        repository.setAccentColor(color)
    }
}