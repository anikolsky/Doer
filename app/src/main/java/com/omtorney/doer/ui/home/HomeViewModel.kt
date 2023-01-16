package com.omtorney.doer.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.data.database.NoteDao
import com.omtorney.doer.domain.AccentColorUseCase
import com.omtorney.doer.domain.LineSeparatorStateUseCase
import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note
import com.omtorney.doer.model.NotePriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDao: NoteDao,
    private val repository: Repository,
    lineSeparatorStateUseCase: LineSeparatorStateUseCase,
    accentColorUseCase: AccentColorUseCase
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

    val lineSeparatorState = lineSeparatorStateUseCase.execute

    val accentColor = accentColorUseCase.execute

    fun addNote(text: String, priority: NotePriority) = viewModelScope.launch {
        val note = Note(
            text = text,
            priority = priority,
            createdAt = LocalDateTime.now(),
            changedAt = LocalDateTime.now(),
            isPinned = false
        )
        repository.insertNote(note)
    }

    fun editNote(text: String, priority: NotePriority) = viewModelScope.launch {
        val note = Note(
            id = selectedNote.value?.id,
            text = text,
            priority = priority,
            createdAt = selectedNote.value?.createdAt,
            changedAt = LocalDateTime.now(),
            isPinned = selectedNote.value?.isPinned ?: false
        )
        repository.insertNote(note)
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
                repository.insertNote(deletedNote!!)
                deletedNote = null
            }
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun resetSelectedNote() {
        _selectedNote.value = null
    }

    fun pinNote(note: Note) {
        val notePinned = note.copy(isPinned = !note.isPinned)
        viewModelScope.launch {
            repository.insertNote(notePinned)
            _selectedNote.value = notePinned
        }
    }
}