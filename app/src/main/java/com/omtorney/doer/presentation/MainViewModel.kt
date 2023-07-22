package com.omtorney.doer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.firebase.auth.SignInResult
import com.omtorney.doer.firebase.auth.SignInState
import com.omtorney.doer.domain.usecases.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsUseCases: SettingsUseCases,
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState= _mainState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsUseCases.getAccentColor().collect { accentColor ->
                _mainState.update { it.copy(accentColor = accentColor) }
            }
            settingsUseCases.getSecondaryColor().collect { secondaryColor ->
                _mainState.update { it.copy(secondaryColor = secondaryColor) }
            }
            settingsUseCases.getNoteSeparatorState().collect { noteSeparatorState ->
                _mainState.update { it.copy(noteSeparatorState = noteSeparatorState) }
            }
            settingsUseCases.getNoteSeparatorSize().collect { noteSeparatorSize ->
                _mainState.update { it.copy(noteSeparatorSize = noteSeparatorSize) }
            }
        }
    }

    /** Sign in */
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _signInState.update { it.copy(
            isSignInSuccessful = result.firestoreUser != null,
            signInErrorMessage = result.errorMessage
        ) }
    }

    fun resetState() {
        _signInState.update { SignInState() }
    }
}
