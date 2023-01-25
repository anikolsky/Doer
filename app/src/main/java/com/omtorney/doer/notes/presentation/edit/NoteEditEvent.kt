package com.omtorney.doer.notes.presentation.edit

sealed class NoteEditEvent {
    data class EnteredText(val text: String) : NoteEditEvent()
    data class Delete(val id: Int) : NoteEditEvent()
    data class SetPriority(val priority: Int) : NoteEditEvent()
    object Pin : NoteEditEvent()
    object Save : NoteEditEvent()
}