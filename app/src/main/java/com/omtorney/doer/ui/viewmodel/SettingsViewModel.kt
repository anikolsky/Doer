package com.omtorney.doer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.domain.AccentColorUseCase
import com.omtorney.doer.domain.LineSeparatorStateUseCase
import com.omtorney.doer.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: Repository,
    lineSeparatorStateUseCase: LineSeparatorStateUseCase,
    accentColorUseCase: AccentColorUseCase
) : ViewModel() {

    val lineSeparatorState = lineSeparatorStateUseCase.execute

    fun setLineSeparatorState(state: Boolean) = viewModelScope.launch {
        repository.setLineSeparatorState(state)
    }

    val accentColor = accentColorUseCase.execute

    fun setAccentColor(color: Long) = viewModelScope.launch {
        repository.setAccentColor(color)
    }
}