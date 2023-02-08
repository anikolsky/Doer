package com.omtorney.doer.goals.domain.usecase

data class GoalUseCases(
    val addGoal: AddGoal,
    val deleteGoal: DeleteGoal,
    val getGoal: GetGoal,
    val getGoals: GetGoals
)