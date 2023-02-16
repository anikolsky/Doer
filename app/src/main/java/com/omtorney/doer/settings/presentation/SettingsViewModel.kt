package com.omtorney.doer.settings.presentation

import android.os.Environment
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.omtorney.doer.notes.domain.usecase.NoteUseCases
import com.omtorney.doer.settings.domain.usecase.SettingsUseCases
import com.omtorney.doer.core.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val backupFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "backup.json"
    )

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

    val lineDividerState = settingsUseCases.getLineDivideState.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    fun setLineDividerState(state: Boolean) = viewModelScope.launch {
        settingsUseCases.setLineDivideState(state)
    }

    fun setAccentColor(color: Color) = viewModelScope.launch {
        settingsUseCases.setAccentColor(color)
    }

    fun setSecondaryColor(color: Color) = viewModelScope.launch {
        settingsUseCases.setSecondaryColor(color)
    }

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