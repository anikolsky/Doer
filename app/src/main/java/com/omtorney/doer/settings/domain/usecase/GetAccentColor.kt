package com.omtorney.doer.settings.domain.usecase

import com.omtorney.doer.core.domain.Repository
import kotlinx.coroutines.flow.Flow

class GetAccentColor(private val repository: Repository) {

    operator fun invoke(): Flow<Long> {
        return repository.getAccentColor
    }
}