package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class GetAccentColor(
    private val repository: Repository
) {

    operator fun invoke(): Flow<Long> {
        return repository.getAccentColor
    }
}