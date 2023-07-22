package com.omtorney.doer.domain.usecases

import com.omtorney.doer.domain.usecases.goals.AddGoal
import com.omtorney.doer.domain.usecases.goals.DeleteGoal
import com.omtorney.doer.domain.usecases.goals.GetGoal
import com.omtorney.doer.domain.usecases.goals.GetGoals

data class GoalUseCases(
    val addGoal: AddGoal,
    val deleteGoal: DeleteGoal,
    val getGoal: GetGoal,
    val getGoals: GetGoals
)