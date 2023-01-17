package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository

class SetAccentColor(
    private val repository: Repository
) {

    suspend operator fun invoke(color: Long) {
        repository.setAccentColor(color)
    }
}