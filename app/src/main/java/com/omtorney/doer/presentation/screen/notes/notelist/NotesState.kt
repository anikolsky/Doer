package com.omtorney.doer.presentation.screen.notes.notelist

import com.omtorney.doer.data.model.note.Note
import com.omtorney.doer.data.model.note.NoteOrder
import com.omtorney.doer.data.model.note.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Priority(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)
