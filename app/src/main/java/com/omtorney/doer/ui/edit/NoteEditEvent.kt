package com.omtorney.doer.ui.edit

import com.omtorney.doer.util.NotePriority

sealed class NoteEditEvent {
    data class EnteredText(val text: String) : NoteEditEvent()
    data class Pin(val isPinned: Boolean) : NoteEditEvent()
    data class Delete(val id: Int) : NoteEditEvent()
    data class SetPriority(val priority: NotePriority) : NoteEditEvent()
    object Save : NoteEditEvent()
}