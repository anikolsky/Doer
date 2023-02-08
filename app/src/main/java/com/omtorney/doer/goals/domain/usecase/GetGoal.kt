package com.omtorney.doer.goals.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.notes.domain.model.Note
import javax.inject.Inject

class GetGoal @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Long): Goal? {
        return repository.getGoalById(id)
    }
}