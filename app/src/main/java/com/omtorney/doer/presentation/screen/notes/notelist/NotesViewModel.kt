package com.omtorney.doer.presentation.screen.notes.notelist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.model.note.NoteOrder
import com.omtorney.doer.data.model.note.OrderType
import com.omtorney.doer.domain.usecases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _notesState = mutableStateOf(NotesState())
    val notesState: State<NotesState> = _notesState

    private var deletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Priority(OrderType.Ascending))
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _notesState.value = notesState.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (notesState.value.noteOrder::class == event.noteOrder::class &&
                    notesState.value.noteOrder.orderType == event.noteOrder.orderType
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
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(deletedNote ?: return@launch)
                    deletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _notesState.value = notesState.value.copy(
                    isOrderSectionVisible = !notesState.value.isOrderSectionVisible
                )
            }
        }
    }
}
