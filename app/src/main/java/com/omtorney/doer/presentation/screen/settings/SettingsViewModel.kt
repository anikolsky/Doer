package com.omtorney.doer.presentation.screen.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.omtorney.doer.data.remote.database.FirestoreResult
import com.omtorney.doer.domain.usecases.FirestoreUseCases
import com.omtorney.doer.domain.usecases.NoteUseCases
import com.omtorney.doer.domain.usecases.SettingsUseCases
import com.omtorney.doer.firebase.presentation.FirestoreUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val settingsUseCases: SettingsUseCases,
    private val firestoreUseCases: FirestoreUseCases,
) : ViewModel() {

    private val auth = Firebase.auth

    val firestoreUserState: StateFlow<FirestoreUserState> = auth.currentUser?.let { user ->
        firestoreUseCases.getUsers(user.displayName!!).map { result ->
            when (result) {
                is FirestoreResult.Success -> FirestoreUserState(data = result.data)
                is FirestoreResult.Error -> FirestoreUserState(errorMessage = result.exception.message)
                is FirestoreResult.Loading -> FirestoreUserState(isLoading = true)
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = FirestoreUserState(isLoading = true),
            started = SharingStarted.WhileSubscribed(5000)
        )
    } ?: flow { emit(FirestoreUserState()) }.stateIn(
        scope = viewModelScope,
        initialValue = FirestoreUserState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetNoteSeparatorState -> {
                viewModelScope.launch {
                    settingsUseCases.setNoteSeparatorState(event.state)
                }
            }

            is SettingsEvent.SetNoteSeparatorSize -> {
                viewModelScope.launch {
                    settingsUseCases.setNoteSeparatorSize(event.size)
                }
            }

            is SettingsEvent.SetAccentColor -> {
                viewModelScope.launch {
                    settingsUseCases.setAccentColor(event.color.toArgb().toLong())
                }
            }

            is SettingsEvent.SetSecondaryColor -> {
                viewModelScope.launch {
                    settingsUseCases.setSecondaryColor(event.color.toArgb().toLong())
                }
            }

            is SettingsEvent.CreateUser -> {
                viewModelScope.launch {
                    val notes = noteUseCases.getNotes().first()
                    firestoreUseCases.createBackup(event.user, notes).collect { result ->
                        when (result) {
                            is FirestoreResult.Success -> showToast(result.data, event.context)
                            is FirestoreResult.Error -> showToast(result.exception.message, event.context)
                            else -> return@collect
                        }
                    }
                }
            }

            is SettingsEvent.DeleteUser -> {
                viewModelScope.launch {
                    firestoreUseCases.deleteBackup(event.user).collect { result ->
                        when (result) {
                            is FirestoreResult.Success -> showToast(result.data, event.context)
                            is FirestoreResult.Error -> showToast(result.exception.message, event.context)
                            else -> return@collect
                        }
                    }
                }
            }

            is SettingsEvent.UpdateUser -> {
                viewModelScope.launch {
                    firestoreUseCases.updateBackup(event.user).collect { result ->
                        when (result) {
                            is FirestoreResult.Success -> showToast(result.data, event.context)
                            is FirestoreResult.Error -> showToast(result.exception.message, event.context)
                            else -> return@collect
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String?, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
