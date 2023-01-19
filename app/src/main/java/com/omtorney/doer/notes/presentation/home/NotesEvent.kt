package com.omtorney.doer.notes.presentation.home

import com.omtorney.doer.core.model.Note
import com.omtorney.doer.notes.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class Delete(val note: Note) : NotesEvent()
    data class Pin(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}