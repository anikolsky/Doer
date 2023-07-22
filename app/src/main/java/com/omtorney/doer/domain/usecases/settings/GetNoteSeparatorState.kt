package com.omtorney.doer.domain.usecases.settings

import com.omtorney.doer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteSeparatorState @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.noteSeparatorState
    }
}
