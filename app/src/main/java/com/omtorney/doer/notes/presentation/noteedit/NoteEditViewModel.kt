package com.omtorney.doer.notes.presentation.noteedit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.core.presentation.components.UiEvent
import com.omtorney.doer.notes.domain.model.InvalidNoteException
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentNoteId: Long? = null

    private val _state = mutableStateOf(NoteEditState())
    val state: State<NoteEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("noteId")?.let { noteId ->
            if (noteId != -1L) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _state.value = state.value.copy(
                            id = note.id,
                            title = note.title,
                            content = note.content,
                            priority = note.priority,
                            status = note.status,
                            createdAt = note.createdAt,
                            modifiedAt = note.modifiedAt,
                            isPinned = note.isPinned
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEditEvent) {
        when (event) {
            is NoteEditEvent.EnteredTitle -> {
                _state.value = state.value.copy(title = event.title)
            }
            is NoteEditEvent.EnteredContent -> {
                _state.value = state.value.copy(content = event.content)
            }
            is NoteEditEvent.Delete -> {
                viewModelScope.launch {
                    val note: Note? = noteUseCases.getNote(event.id)
                    note?.let { noteUseCases.deleteNote(it) }
                }
            }
            is NoteEditEvent.Pin -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(isPinned = !state.value.isPinned)
                }
            }
            is NoteEditEvent.Save -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = state.value.title,
                                content = state.value.content,
                                priority = state.value.priority,
                                status = state.value.status,
                                createdAt = if (state.value.createdAt == 0L) {
                                    System.currentTimeMillis()
                                } else {
                                    state.value.createdAt
                                },
                                modifiedAt = System.currentTimeMillis(),
                                isPinned = state.value.isPinned
                            )
                        )
                        _eventFlow.emit(UiEvent.Save)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is NoteEditEvent.SetPriority -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(priority = event.priority)
                }
            }
        }
    }
}