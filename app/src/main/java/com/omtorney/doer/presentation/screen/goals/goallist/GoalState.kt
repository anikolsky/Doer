package com.omtorney.doer.presentation.screen.goals.goallist

import com.omtorney.doer.data.model.goal.Goal

data class GoalState(
    val goals: List<Goal> = emptyList(),
//    val noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
//    val isOrderSectionVisible: Boolean = false
)
