package com.omtorney.doer.notes.presentation.noteedit

sealed class NoteEditEvent {
    data class EnteredTitle(val title: String) : NoteEditEvent()
    data class EnteredContent(val content: String) : NoteEditEvent()
    data class Delete(val id: Long) : NoteEditEvent()
    data class SetPriority(val priority: Int) : NoteEditEvent()
    object Pin : NoteEditEvent()
    object Save : NoteEditEvent()
}