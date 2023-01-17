package com.omtorney.doer.ui.edit

import com.omtorney.doer.model.Note
import com.omtorney.doer.util.NotePriority

sealed class NoteEditEvent {
    data class EnteredText(val text: String) : NoteEditEvent()
    data class Pin(val pinState: Boolean) : NoteEditEvent()
    data class Delete(val note: Note) : NoteEditEvent()
    object SaveNoteEdit : NoteEditEvent()
    data class SetPriority(val priority: NotePriority) : NoteEditEvent()
}
