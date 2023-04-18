package com.omtorney.doer.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.core.util.Constants
import com.omtorney.doer.settings.data.SettingsStore
import com.omtorney.doer.settings.presentation.signin.SignInResult
import com.omtorney.doer.settings.presentation.signin.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsDataStore: SettingsStore
) : ViewModel() {

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

    val noteSeparatorState = settingsDataStore.getNoteSeparatorState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    val noteSeparatorSize = settingsDataStore.getNoteSeparatorSize.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 8
    )

    /** Sign in */
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _signInState.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInErrorMessage = result.errorMessage
        ) }
    }

    fun resetState() {
        _signInState.update { SignInState() }
    }
}