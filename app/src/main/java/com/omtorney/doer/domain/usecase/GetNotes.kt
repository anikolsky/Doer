package com.omtorney.doer.domain.usecase

import com.omtorney.doer.domain.Repository
import com.omtorney.doer.model.Note
import com.omtorney.doer.util.NoteOrder
import com.omtorney.doer.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: Repository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.DateCreated -> notes.sortedBy { it.createdAt }
                        is NoteOrder.DateModified -> notes.sortedBy { it.modifiedAt }
                        is NoteOrder.Text -> notes.sortedBy { it.text.lowercase() }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.DateCreated -> notes.sortedByDescending { it.createdAt }
                        is NoteOrder.DateModified -> notes.sortedByDescending { it.modifiedAt }
                        is NoteOrder.Text -> notes.sortedByDescending { it.text.lowercase() }
                    }
                }
            }
        }
    }
}