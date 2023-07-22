package com.omtorney.doer.domain.usecases

import com.omtorney.doer.domain.usecases.notes.AddNote
import com.omtorney.doer.domain.usecases.notes.DeleteNote
import com.omtorney.doer.domain.usecases.notes.GetNote
import com.omtorney.doer.domain.usecases.notes.GetNotes
import com.omtorney.doer.domain.usecases.notes.PinNote

data class NoteUseCases(
    val getNotes: GetNotes,
    val getNote: GetNote,
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val pinNote: PinNote
)
