package com.omtorney.doer.settings.presentation

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.omtorney.doer.firestore.data.FirestoreResult
import com.omtorney.doer.firestore.data.FirestoreUser
import com.omtorney.doer.firestore.domain.usecase.FirestoreUseCases
import com.omtorney.doer.firestore.presentation.FirestoreUserState
import com.omtorney.doer.notes.domain.usecase.NoteUseCases
import com.omtorney.doer.settings.data.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val firestoreUseCases: FirestoreUseCases,
    private val settingsDataStore: SettingsStore
) : ViewModel() {

    fun setNoteSeparatorState(state: Boolean) = viewModelScope.launch {
        settingsDataStore.setNoteSeparatorState(state)
    }

    fun setNoteSeparatorSize(size: Int) = viewModelScope.launch {
        settingsDataStore.setNoteSeparatorSize(size)
    }

    fun setAccentColor(color: Color) = viewModelScope.launch {
        settingsDataStore.setAccentColor(color.toArgb().toLong())
    }

    fun setSecondaryColor(color: Color) = viewModelScope.launch {
        settingsDataStore.setSecondaryColor(color.toArgb().toLong())
    }

    /** Firestore database backup */
    val firestoreUserState: StateFlow<FirestoreUserState> = firestoreUseCases.getUsers().map { result ->
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

    fun createBackup(
        firestoreUserName: String,
        context: Context
    ) {
        viewModelScope.launch {
            val notes = noteUseCases.getNotes().first()
            firestoreUseCases.createBackup(firestoreUserName, notes).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    }
                    is FirestoreResult.Error -> {
                        Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        return@collect
                    }
                }
            }
        }
    }

    fun updateBackup(firestoreUser: FirestoreUser, context: Context) {
        viewModelScope.launch {
            firestoreUseCases.updateBackup(firestoreUser).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    }
                    is FirestoreResult.Error -> {
                        Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        return@collect
                    }
                }
            }
        }
    }

    fun deleteBackup(firestoreUser: FirestoreUser, context: Context) {
        viewModelScope.launch {
            firestoreUseCases.deleteBackup(firestoreUser).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> {
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    }
                    is FirestoreResult.Error -> {
                        Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        return@collect
                    }
                }
            }
        }
    }

    /** Database local backup */
    private val backupFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "backup.json"
    )

    fun backupDatabase() { // FIXME
        val gson = GsonBuilder().setPrettyPrinting().create()
        viewModelScope.launch {
            val notes = noteUseCases.getNotes().toList().first()
            Log.d("TESTLOG", "notes: $notes")
            val jsonData = gson.toJson(notes)
            Log.d("TESTLOG", "jsonData: $jsonData")
            try {
                backupFile.writeText(jsonData)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TESTLOG", "error: ${e.message}")
            }
        }
    }

    fun restoreDatabase() {
//        val gson = Gson()
//        val jsonData = backupFile.readText()
//        val notes = gson.fromJson(jsonData, Array<Note>::class.java).toList()
//        viewModelScope.launch {
//            noteDao.insertAll(notes)
//        }
    }
}
