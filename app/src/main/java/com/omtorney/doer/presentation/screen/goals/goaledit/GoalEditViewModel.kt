package com.omtorney.doer.presentation.screen.goals.goaledit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.presentation.components.UiEvent
import com.omtorney.doer.data.model.goal.Goal
import com.omtorney.doer.data.model.goal.InvalidGoalException
import com.omtorney.doer.data.model.goal.Step
import com.omtorney.doer.domain.usecases.GoalUseCases
import com.omtorney.doer.presentation.screen.goals.components.calculateProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalEditViewModel @Inject constructor(
    private val goalUseCases: GoalUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentGoalId: Long? = null

    private val _goalEditState = mutableStateOf(GoalEditState())
    val goalEditState: State<GoalEditState> = _goalEditState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("goalId")?.let { goalId ->
            if (goalId != -1L) {
                viewModelScope.launch {
                    goalUseCases.getGoal(goalId)?.also { goal ->
                        currentGoalId = goal.id
                        _goalEditState.value = goalEditState.value.copy(
                            id = goal.id,
                            title = goal.title,
                            steps = goal.steps,
                            progress = calculateProgress(goal.steps),
                            createdAt = goal.createdAt,
                            modifiedAt = goal.modifiedAt,
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: GoalEditEvent) {
        when (event) {
            is GoalEditEvent.EnterTitle -> {
                _goalEditState.value = goalEditState.value.copy(title = event.title)
            }
            is GoalEditEvent.AddStep -> {
                _goalEditState.value = goalEditState.value.copy(steps = goalEditState.value.steps.toMutableList().apply {
                    add(Step(id = event.id + 1))
                })
            }
            is GoalEditEvent.EditStep -> {
                val updatedSteps = goalEditState.value.steps.map { step ->
                    if (step.id == event.id) {
                        step.copy(text = event.text)
                    } else {
                        step
                    }
                }.toMutableList()
                _goalEditState.value = goalEditState.value.copy(steps = updatedSteps)
            }
            is GoalEditEvent.AchieveStep -> {
                val updatedSteps = goalEditState.value.steps.map { step ->
                    if (step.id == event.id) {
                        step.copy(isAchieved = !step.isAchieved)
                    } else {
                        step
                    }
                }.toMutableList()
                _goalEditState.value = goalEditState.value.copy(
                    steps = updatedSteps,
                    progress = calculateProgress(updatedSteps)
                )
            }
            is GoalEditEvent.DeleteStep -> {
                val updatedSteps = goalEditState.value.steps.filter { step -> step.id != event.id }.toMutableList()
                _goalEditState.value = goalEditState.value.copy(steps = updatedSteps)
            }
            is GoalEditEvent.DeleteGoal -> {
                viewModelScope.launch {
                    val goal: Goal? = goalUseCases.getGoal(event.id)
                    goal?.let { goalUseCases.deleteGoal(it) }
                }
            }
            is GoalEditEvent.SaveGoal -> {
                viewModelScope.launch {
                    if (goalEditState.value.title.isEmpty()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Enter goal title"))
                    } else if (goalEditState.value.steps.isEmpty()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Enter at least one step"))
                    } else {
                        try {
                            goalUseCases.addGoal(
                                Goal(
                                    id = currentGoalId,
                                    title = goalEditState.value.title,
                                    steps = goalEditState.value.steps,
                                    createdAt = if (goalEditState.value.createdAt == 0L) {
                                        System.currentTimeMillis()
                                    } else {
                                        goalEditState.value.createdAt
                                    },
                                    modifiedAt = System.currentTimeMillis()
                                )
                            )
                            _eventFlow.emit(UiEvent.Save)
                        } catch (e: InvalidGoalException) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = e.message ?: "Couldn't save goal"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
