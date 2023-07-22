package com.omtorney.doer.presentation.screen.notes.noteedit

import com.omtorney.doer.data.model.note.NotePriority

sealed class NoteEditEvent {
    data class EnteredTitle(val title: String) : NoteEditEvent()
    data class EnteredContent(val content: String) : NoteEditEvent()
    data class Delete(val id: Long) : NoteEditEvent()
    data class SetPriority(val priority: NotePriority) : NoteEditEvent()
    object Pin : NoteEditEvent()
    object Save : NoteEditEvent()
}
