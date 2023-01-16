package com.omtorney.doer.domain.usecase

data class NoteUseCases(
    val getNotes: GetNotes,
    val addNote: AddNote,
    val deleteNote: DeleteNote
)