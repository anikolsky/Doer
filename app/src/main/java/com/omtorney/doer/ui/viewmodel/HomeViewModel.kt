package com.omtorney.doer.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.data.database.NoteDao
import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao,
    private val repository: Repository
) : ViewModel() {

    private val _selectedNote: MutableState<Note?> = mutableStateOf(null)
    val selectedNote: State<Note?>
        get() = _selectedNote

    private var deletedNote: Note? = null

    val allNotes = this.noteDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val lineSeparatorState = repository.getLineSeparatorState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    val accentColor = repository.getAccentColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = repository.getInitialColor
    )

    fun addNote(noteText: String) = viewModelScope.launch {
        val note = Note(noteText = noteText)
        repository.addNote(note)
    }

    fun editNote(noteText: String) = viewModelScope.launch {
        val note = Note(id = selectedNote.value?.id, noteText = noteText)
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) {
        deletedNote = note
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun undoDeleteNote() {
        if (deletedNote != null)
            viewModelScope.launch {
                repository.addNote(deletedNote!!)
                deletedNote = null
            }
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun resetSelectedNote() {
        _selectedNote.value = null
    }
}