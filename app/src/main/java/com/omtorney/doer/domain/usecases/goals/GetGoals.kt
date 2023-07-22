package com.omtorney.doer.domain.usecases.goals

import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.model.goal.Goal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoals @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(): Flow<List<Goal>> {
        return repository.getGoals()
    }
}
