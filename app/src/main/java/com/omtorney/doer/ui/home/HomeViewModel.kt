package com.omtorney.doer.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.domain.usecase.NoteUseCases
import com.omtorney.doer.domain.usecase.SettingsUseCases
import com.omtorney.doer.model.InvalidNoteException
import com.omtorney.doer.model.Note
import com.omtorney.doer.ui.notes.NotesState
import com.omtorney.doer.util.Constants
import com.omtorney.doer.util.NoteOrder
import com.omtorney.doer.util.NotePriority
import com.omtorney.doer.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val settingsUseCases: SettingsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var deletedNote: Note? = null

    private var getNotesJob: Job? = null

//    private val _selectedNote: MutableState<Note?> = mutableStateOf(null)
//    val selectedNote: State<Note?>
//        get() = _selectedNote

    val accentColor = settingsUseCases.getAccentColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.initialColor
    )

    val lineDividerState = settingsUseCases.getLineDivideState.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

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
            is NotesEvent.Pin -> {
                viewModelScope.launch {
                    val notePinned = event.note.copy(isPinned = !event.note.isPinned)
                    noteUseCases.addNote(notePinned)
//                    event.note = notePinned
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

//    fun addNote(text: String, priority: NotePriority) = viewModelScope.launch {
//        val note = Note(
//            text = text,
//            priority = priority,
//            createdAt = LocalDateTime.now(),
//            modifiedAt = LocalDateTime.now(),
//            isPinned = false
//        )
//        try {
//            noteUseCases.addNote(note)
//        } catch (e: InvalidNoteException) {
//            Log.d("TESTLOG", "addNote: ${e.message}")
//        }
//    }
//
//    fun editNote(text: String, priority: NotePriority) = viewModelScope.launch {
//        val note = Note(
//            id = selectedNote.value?.id,
//            text = text,
//            priority = priority,
//            createdAt = selectedNote.value?.createdAt,
//            modifiedAt = LocalDateTime.now(),
//            isPinned = selectedNote.value?.isPinned ?: false
//        )
//        noteUseCases.addNote(note)
//    }

//    fun selectNote(note: Note) {
//        _selectedNote.value = note
//    }

//    fun resetSelectedNote() {
//        _selectedNote.value = null
//    }
}