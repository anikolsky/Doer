package com.omtorney.doer.presentation.screen.goals.goaledit

sealed class GoalEditEvent {
    data class EnterTitle(val title: String) : GoalEditEvent()
    data class AddStep(val id: Int) : GoalEditEvent()
    data class EditStep(val id: Int, val text: String) : GoalEditEvent()
    data class AchieveStep(val id: Int) : GoalEditEvent()
    data class DeleteStep(val id: Int) : GoalEditEvent()
    data class DeleteGoal(val id: Long) : GoalEditEvent()
    object SaveGoal : GoalEditEvent()
}
