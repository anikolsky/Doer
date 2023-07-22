package com.omtorney.doer.presentation.screen.notes.noteedit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.data.model.note.InvalidNoteException
import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.domain.usecases.NoteUseCases
import com.omtorney.doer.presentation.components.UiEvent
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

    private var currentNoteId: Long = 0

    private val _noteEditState = mutableStateOf(NoteEditState())
    val noteEditState: State<NoteEditState> = _noteEditState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("noteId")?.let { noteId ->
            if (noteId != -1L) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteEditState.value = noteEditState.value.copy(
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
                _noteEditState.value = noteEditState.value.copy(title = event.title)
            }
            is NoteEditEvent.EnteredContent -> {
                _noteEditState.value = noteEditState.value.copy(content = event.content)
            }
            is NoteEditEvent.Delete -> {
                viewModelScope.launch {
                    val note: Note? = noteUseCases.getNote(event.id)
                    note?.let { noteUseCases.deleteNote(it) }
                }
            }
            is NoteEditEvent.Pin -> {
                viewModelScope.launch {
                    _noteEditState.value = noteEditState.value.copy(isPinned = !noteEditState.value.isPinned)
                }
            }
            is NoteEditEvent.Save -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                title = noteEditState.value.title,
                                content = noteEditState.value.content,
                                priority = noteEditState.value.priority,
                                status = noteEditState.value.status,
                                createdAt = if (noteEditState.value.createdAt == 0L) {
                                    System.currentTimeMillis()
                                } else {
                                    noteEditState.value.createdAt
                                },
                                modifiedAt = System.currentTimeMillis(),
                                isPinned = noteEditState.value.isPinned
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
                    _noteEditState.value = noteEditState.value.copy(priority = event.priority)
                }
            }
        }
    }
}
