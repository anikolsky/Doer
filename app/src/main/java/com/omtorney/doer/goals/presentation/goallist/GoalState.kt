package com.omtorney.doer.goals.presentation.goallist

import com.omtorney.doer.goals.domain.model.Goal

data class GoalState(
    val goals: List<Goal> = emptyList(),
//    val noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
//    val isOrderSectionVisible: Boolean = false
)