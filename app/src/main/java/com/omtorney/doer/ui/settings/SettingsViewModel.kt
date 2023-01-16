package com.omtorney.doer.ui.settings

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.omtorney.doer.data.database.NoteDao
import com.omtorney.doer.domain.*
import com.omtorney.doer.domain.usecase.AccentColor
import com.omtorney.doer.domain.usecase.LineSeparatorState
import com.omtorney.doer.domain.usecase.NoteUseCases
import com.omtorney.doer.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val noteDao: NoteDao, // TODO get rid of
    private val repository: Repository, // TODO get rid of
    lineSeparatorState: LineSeparatorState,
    accentColor: AccentColor
) : ViewModel() {

    private val backupFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "backup.json"
    )

    val lineSeparatorState = lineSeparatorState.execute

    fun setLineSeparatorState(state: Boolean) = viewModelScope.launch {
        repository.setLineSeparatorState(state)
    }

    val accentColor = accentColor.execute

    fun setAccentColor(color: Long) = viewModelScope.launch {
        repository.setAccentColor(color)
    }

    fun backupDatabase() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val notes = noteUseCases.getNotes
        Log.d("TESTLOG", "notes: $notes")
        val jsonData = gson.toJson(notes)
        Log.d("TESTLOG", "jsonData: $jsonData")
        try {
            backupFile.writeText(jsonData)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TESTLOG", "e: ${e.message}")
        }
    }

    fun restoreDatabase() {
        val gson = Gson()
        val jsonData = backupFile.readText()
        val notes = gson.fromJson(jsonData, Array<Note>::class.java).toList()
        viewModelScope.launch {
            noteDao.insertAll(notes)
        }
    }
}