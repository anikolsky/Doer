package com.omtorney.doer.goals.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.goals.domain.model.InvalidGoalException
import javax.inject.Inject

class AddGoal @Inject constructor(
    private val repository: Repository
) {

    @Throws(InvalidGoalException::class)
    suspend operator fun invoke(goal: Goal) {
        if (goal.title.isBlank()) {
            throw InvalidGoalException("Goal title can't be empty")
        }
        repository.insertGoal(goal)
    }
}