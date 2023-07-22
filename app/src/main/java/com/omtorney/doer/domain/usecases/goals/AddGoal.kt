package com.omtorney.doer.domain.usecases.goals

import com.omtorney.doer.domain.repository.Repository
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.goal.InvalidGoalException
import javax.inject.Inject

class AddGoal @Inject constructor(
    private val repository: Repository
) {

    @Throws(InvalidGoalException::class)
    suspend operator fun invoke(goal: Goal) {
        if (goal.title.isBlank()) {
            throw InvalidGoalException("GoalEdit title can't be empty")
        }
        repository.insertGoal(goal)
    }
}
