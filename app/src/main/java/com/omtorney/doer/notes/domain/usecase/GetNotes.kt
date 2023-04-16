package com.omtorney.doer.notes.domain.usecase

import com.omtorney.doer.core.domain.Repository
import com.omtorney.doer.notes.domain.model.Note
import com.omtorney.doer.notes.domain.model.NoteOrder
import com.omtorney.doer.notes.domain.model.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotes @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.DateCreated(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Priority -> notes.sortedBy { it.priority }
                        is NoteOrder.DateCreated -> notes.sortedBy { it.createdAt }
                        is NoteOrder.DateModified -> notes.sortedBy { it.modifiedAt }
                        is NoteOrder.Text -> notes.sortedBy { it.title[0].lowercase() }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Priority -> notes.sortedByDescending { it.priority }
                        is NoteOrder.DateCreated -> notes.sortedByDescending { it.createdAt }
                        is NoteOrder.DateModified -> notes.sortedByDescending { it.modifiedAt }
                        is NoteOrder.Text -> notes.sortedByDescending { it.title[0].lowercase() }
                    }
                }
            }
        }
    }
}