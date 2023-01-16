package com.omtorney.doer.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.domain.*
import com.omtorney.doer.domain.usecase.AccentColor
import com.omtorney.doer.domain.usecase.LineSeparatorState
import com.omtorney.doer.domain.usecase.NoteUseCases
import com.omtorney.doer.model.Note
import com.omtorney.doer.ui.notes.NotesEvent
import com.omtorney.doer.ui.notes.NotesState
import com.omtorney.doer.util.NoteOrder
import com.omtorney.doer.util.NotePriority
import com.omtorney.doer.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository, // TODO get rid of
    private val noteUseCases: NoteUseCases,
    lineSeparatorState: LineSeparatorState, // TODO get rid of
    accentColor: AccentColor // TODO get rid of
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var deletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.DateCreated(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.Delete -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    deletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(deletedNote ?: return@launch)
                    deletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }


    private val _selectedNote: MutableState<Note?> = mutableStateOf(null)
    val selectedNote: State<Note?>
        get() = _selectedNote


    val allNotes = getNotesUseCase.getNotes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val lineSeparatorState = lineSeparatorState.execute

    val accentColor = accentColor.execute

    fun addNote(text: String, priority: NotePriority) = viewModelScope.launch {
        val note = Note(
            text = text,
            priority = priority,
            createdAt = LocalDateTime.now(),
            modifiedAt = LocalDateTime.now(),
            isPinned = false
        )
        repository.insertNote(note) // TODO get rid of
    }

    fun editNote(text: String, priority: NotePriority) = viewModelScope.launch {
        val note = Note(
            id = selectedNote.value?.id,
            text = text,
            priority = priority,
            createdAt = selectedNote.value?.createdAt,
            modifiedAt = LocalDateTime.now(),
            isPinned = selectedNote.value?.isPinned ?: false
        )
        repository.insertNote(note) // TODO get rid of
    }

    fun deleteNote(note: Note) {
        deletedNote = note

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
            repository.insertNote(notePinned) // TODO get rid of
            _selectedNote.value = notePinned
        }
    }
}