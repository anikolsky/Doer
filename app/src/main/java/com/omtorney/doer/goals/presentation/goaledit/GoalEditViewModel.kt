package com.omtorney.doer.goals.presentation.goaledit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.core.presentation.components.UiEvent
import com.omtorney.doer.core.util.Constants
import com.omtorney.doer.goals.domain.model.Goal
import com.omtorney.doer.goals.domain.model.InvalidGoalException
import com.omtorney.doer.goals.domain.model.Step
import com.omtorney.doer.goals.domain.usecase.GoalUseCases
import com.omtorney.doer.goals.presentation.components.calculateProgress
import com.omtorney.doer.settings.domain.usecase.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalEditViewModel @Inject constructor(
    private val goalUseCases: GoalUseCases,
    private val settingsUseCases: SettingsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentGoalId: Long? = null

    private val _state = mutableStateOf(GoalEditState())
    val state: State<GoalEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val accentColor = settingsUseCases.getAccentColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.INITIAL_ACCENT_COLOR
    )

    val secondaryColor = settingsUseCases.getSecondaryColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.INITIAL_SECONDARY_COLOR
    )

    init {
        savedStateHandle.get<Long>("goalId")?.let { goalId ->
            if (goalId != -1L) {
                viewModelScope.launch {
                    goalUseCases.getGoal(goalId)?.also { goal ->
                        currentGoalId = goal.id
                        _state.value = state.value.copy(
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
                _state.value = state.value.copy(title = event.title)
            }
            is GoalEditEvent.AddStep -> {
                _state.value = state.value.copy(steps = state.value.steps.toMutableList().apply {
                    add(Step(id = event.id + 1))
                })
            }
            is GoalEditEvent.EditStep -> {
                val updatedSteps = state.value.steps.map { step ->
                    if (step.id == event.id) {
                        step.copy(text = event.text)
                    } else {
                        step
                    }
                }.toMutableList()
                _state.value = state.value.copy(steps = updatedSteps)
            }
            is GoalEditEvent.AchieveStep -> {
                val updatedSteps = state.value.steps.map { step ->
                    if (step.id == event.id) {
                        step.copy(isAchieved = !step.isAchieved)
                    } else {
                        step
                    }
                }.toMutableList()
                _state.value = state.value.copy(
                    steps = updatedSteps,
                    progress = calculateProgress(updatedSteps)
                )
            }
            is GoalEditEvent.DeleteStep -> {
                val updatedSteps = state.value.steps.filter { step -> step.id != event.id }.toMutableList()
                _state.value = state.value.copy(steps = updatedSteps)
            }
            is GoalEditEvent.DeleteGoal -> {
                viewModelScope.launch {
                    val goal: Goal? = goalUseCases.getGoal(event.id)
                    goal?.let { goalUseCases.deleteGoal(it) }
                }
            }
            is GoalEditEvent.SaveGoal -> {
                viewModelScope.launch {
                    if (state.value.title.isEmpty()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Enter goal title"))
                    } else if (state.value.steps.isEmpty()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = "Enter at least one step"))
                    } else {
                        try {
                            goalUseCases.addGoal(
                                Goal(
                                    id = currentGoalId,
                                    title = state.value.title,
                                    steps = state.value.steps,
                                    createdAt = if (state.value.createdAt == 0L) {
                                        System.currentTimeMillis()
                                    } else {
                                        state.value.createdAt
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