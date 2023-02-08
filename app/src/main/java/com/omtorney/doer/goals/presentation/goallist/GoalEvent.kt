package com.omtorney.doer.goals.presentation.goallist

import com.omtorney.doer.goals.domain.model.Goal

sealed class GoalEvent {
//    data class Order(val noteOrder: NoteOrder) : GoalEvent()
    data class Delete(val goal: Goal) : GoalEvent()
//    data class Pin(val note: Note) : GoalEvent()
//    object RestoreNote : GoalEvent()
//    object ToggleOrderSection : GoalEvent()
}