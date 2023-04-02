package com.omtorney.doer.goals.presentation.goallist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.core.util.Constants
import com.omtorney.doer.goals.domain.usecase.GoalUseCases
import com.omtorney.doer.settings.data.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalUseCases: GoalUseCases,
    settingsDataStore: SettingsStore
) : ViewModel() {

    private val _state = mutableStateOf(GoalState())
    val state: State<GoalState> = _state

    val accentColor: StateFlow<Long> = settingsDataStore.getAccentColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.INITIAL_ACCENT_COLOR
    )

    val secondaryColor: StateFlow<Long> = settingsDataStore.getSecondaryColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.INITIAL_SECONDARY_COLOR
    )

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