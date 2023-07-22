package com.omtorney.doer.domain.usecases.goals

import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.model.goal.Goal
import javax.inject.Inject

class GetGoal @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Long): Goal? {
        return repository.getGoalById(id)
    }
}
