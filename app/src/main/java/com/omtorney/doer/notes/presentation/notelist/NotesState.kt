package com.omtorney.doer.notes.presentation.notelist

import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.util.NoteOrder
import com.omtorney.doer.notes.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)