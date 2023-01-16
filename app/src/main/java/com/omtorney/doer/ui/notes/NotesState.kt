package com.omtorney.doer.ui.notes

import com.omtorney.doer.model.Note
import com.omtorney.doer.util.NoteOrder
import com.omtorney.doer.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)