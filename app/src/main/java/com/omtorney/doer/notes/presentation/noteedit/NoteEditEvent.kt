package com.omtorney.doer.notes.presentation.noteedit

sealed class NoteEditEvent {
    data class EnteredText(val text: String) : NoteEditEvent()
    data class Delete(val id: Long) : NoteEditEvent()
    data class SetPriority(val priority: Int) : NoteEditEvent()
    object Pin : NoteEditEvent()
    object Save : NoteEditEvent()
}