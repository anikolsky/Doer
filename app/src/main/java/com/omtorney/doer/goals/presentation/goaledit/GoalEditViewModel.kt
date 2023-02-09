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
import com.omtorney.doer.settings.domain.usecase.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
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
        initialValue = Constants.INITIAL_COLOR
    )

    val secondaryColor = settingsUseCases.getSecondaryColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.INITIAL_COLOR
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
                _state.value = state.value.copy(steps = state.value.steps.plus(Step(id = event.id + 1)))
            }
            is GoalEditEvent.EditStep -> {
                _state.value = state.value.copy(steps = // TODO updated list)
            }
            is GoalEditEvent.AchieveStep -> {

            }
            is GoalEditEvent.DeleteStep -> {

            }
            is GoalEditEvent.DeleteGoal -> {
                viewModelScope.launch {
                    val goal: Goal? = goalUseCases.getGoal(event.id)
                    goal?.let { goalUseCases.deleteGoal(it) }
                }
            }
            is GoalEditEvent.SaveGoal -> {
                viewModelScope.launch {
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