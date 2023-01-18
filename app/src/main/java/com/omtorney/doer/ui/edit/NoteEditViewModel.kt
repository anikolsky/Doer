package com.omtorney.doer.ui.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.doer.domain.usecase.NoteUseCases
import com.omtorney.doer.domain.usecase.SettingsUseCases
import com.omtorney.doer.model.InvalidNoteException
import com.omtorney.doer.model.Note
import com.omtorney.doer.util.Constants
import com.omtorney.doer.util.NotePriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val settingsUseCases: SettingsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

//    private val _state = mutableStateOf(NoteEditState())
//    val state: State<NoteEditState> = _state

    private var currentNoteId: Int? = null

    private val _noteText = mutableStateOf(NoteEditState())
    val noteText: State<NoteEditState> = _noteText

    private val _priority = mutableStateOf(NoteEditState())
    val priority: State<NoteEditState> = _priority

    private val _isPinned = mutableStateOf(NoteEditState())
    val isPinned: State<NoteEditState> = _isPinned

    private val _id = mutableStateOf(NoteEditState())
    val id: State<NoteEditState> = _id

    private val _createdAt = mutableStateOf(NoteEditState())
    val createdAt: State<NoteEditState> = _createdAt

    private val _modifiedAt = mutableStateOf(NoteEditState())
    val modifiedAt: State<NoteEditState> = _modifiedAt



    val accentColor = settingsUseCases.getAccentColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.initialColor
    )


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteText.value = noteText.value.copy(text = note.text)
                        _priority.value = priority.value.copy(priority = note.priority)
                        _isPinned.value = isPinned.value.copy(isPinned = note.isPinned)
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEditEvent) {
        when (event) {
            is NoteEditEvent.EnteredText -> {
                _noteText.value = noteText.value.copy(text = event.text)
            }
            is NoteEditEvent.Delete -> {
                viewModelScope.launch {
                    val note: Note? = noteUseCases.getNote(event.id)
                    note?.let { noteUseCases.deleteNote(it) }
                }
            }
            is NoteEditEvent.Pin -> {
                viewModelScope.launch {
                    _isPinned.value = isPinned.value.copy(isPinned = event.isPinned)
//                    val notePinned = event.note.copy(isPinned = !event.note.isPinned)
//                    noteUseCases.addNote(notePinned)
//                    event.note = notePinned
                }
            }
            is NoteEditEvent.Save -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                text = noteText.value.text,
                                priority = priority.value.priority,
                                createdAt = System.currentTimeMillis(),
                                modifiedAt = System.currentTimeMillis(),
                                isPinned = false
                            )
                        )
                    } catch (e: InvalidNoteException) {
                        // TODO catch InvalidNoteException
                    }
                }
            }
            is NoteEditEvent.SetPriority -> {
                viewModelScope.launch {
                    _priority.value = priority.value.copy(priority = event.priority)
                }
            }
        }
    }
}