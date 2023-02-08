package com.omtorney.doer.goals.presentation.goaledit

sealed class GoalEditEvent {
    data class EnteredTitle(val title: String) : GoalEditEvent()
    data class Delete(val id: Long) : GoalEditEvent()
//    data class SetPriority(val priority: Int) : GoalEditEvent()
//    object Pin : GoalEditEvent()
    object Save : GoalEditEvent()
}