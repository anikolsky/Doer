package com.omtorney.doer.notes.presentation.noteedit

import com.omtorney.doer.notes.domain.model.NoteStatus

data class NoteEditState(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val priority: Int = 4,
    val status: NoteStatus = NoteStatus.ToDo,
    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)