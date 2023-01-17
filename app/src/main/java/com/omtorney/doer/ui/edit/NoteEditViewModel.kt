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

    val accentColor = settingsUseCases.getAccentColor.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Constants.initialColor
    )

    private val _noteText = mutableStateOf(NoteEditState())
    val noteText: State<NoteEditState> = _noteText

    private var currentNoteId: Long? = null

    // TODO NoteEditState
    private val _isPinned = mutableStateOf(false)
    val isPinned: State<Boolean> = _isPinned

    private val _priority = mutableStateOf(NotePriority.No)
    val priority: State<NotePriority> = _priority

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteText.value = noteText.value.copy(
                            text = note.text
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEditEvent) {
        when (event) {
            is NoteEditEvent.Delete -> {
//                noteUseCases.deleteNote()
            }
            is NoteEditEvent.EnteredText -> {}
            is NoteEditEvent.Pin -> {
                viewModelScope.launch {
                    val notePinned = event.note.copy(isPinned = !event.note.isPinned)
                    noteUseCases.addNote(notePinned)
//                    event.note = notePinned
                }
            }
            NoteEditEvent.SaveNoteEdit -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                id = currentNoteId,
                                text = noteText.value.text,
                                priority = priority.value,
                                createdAt = System.currentTimeMillis(),
                                modifiedAt = System.currentTimeMillis(),
                                isPinned = false
                            )
                        )
                    } catch (e: InvalidNoteException) {

                    }
                }
            }
            is NoteEditEvent.SetPriority -> {}
        }
    }
}