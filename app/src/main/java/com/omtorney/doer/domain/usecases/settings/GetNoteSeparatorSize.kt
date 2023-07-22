package com.omtorney.doer.domain.usecases.settings

import com.omtorney.doer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteSeparatorSize @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Int> {
        return repository.noteSeparatorSize
    }
}
