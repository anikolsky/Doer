package com.omtorney.doer.presentation.screen.notes.noteedit

import com.omtorney.doer.data.model.note.NotePriority
import com.omtorney.doer.data.model.note.NoteStatus

data class NoteEditState(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val priority: NotePriority = NotePriority.No,
    val status: NoteStatus = NoteStatus.ToDo,
    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)
