package com.omtorney.doer.goals.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.goals.domain.model.Goal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoals @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(): Flow<List<Goal>> {
        return repository.getGoals()
    }
}