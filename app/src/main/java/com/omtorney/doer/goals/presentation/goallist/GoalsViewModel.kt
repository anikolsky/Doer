package com.omtorney.doer.goals.presentation.goallist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.goals.domain.usecase.GoalUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalUseCases: GoalUseCases
) : ViewModel() {

    private val _state = mutableStateOf(GoalState())
    val state: State<GoalState> = _state

    init {
        getGoals()
    }

    private fun getGoals() {
        goalUseCases.getGoals().onEach { goals ->
            _state.value = state.value.copy(goals = goals)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.Delete -> {
                viewModelScope.launch {
                    goalUseCases.deleteGoal(event.goal)
                }
            }
        }
    }
}