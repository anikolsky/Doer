package com.omtorney.doer.ui.notes

import com.omtorney.doer.model.Note
import com.omtorney.doer.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
//    data class Add(val note: Note): NotesEvent()
    data class Delete(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}