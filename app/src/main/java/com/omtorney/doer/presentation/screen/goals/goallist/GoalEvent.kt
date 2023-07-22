package com.omtorney.doer.presentation.screen.goals.goallist

import com.omtorney.doer.data.model.goal.Goal

sealed class GoalEvent {
//    data class Order(val noteOrder: NoteOrder) : GoalEvent()
    data class Delete(val goal: Goal) : GoalEvent()
//    data class Pin(val note: NoteEdit) : GoalEvent()
//    object RestoreNote : GoalEvent()
//    object ToggleOrderSection : GoalEvent()
}
