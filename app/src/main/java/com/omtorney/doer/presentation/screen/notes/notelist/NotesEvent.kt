package com.omtorney.doer.presentation.screen.notes.notelist

import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.model.note.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class Delete(val note: Note) : NotesEvent()
    data class Pin(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
