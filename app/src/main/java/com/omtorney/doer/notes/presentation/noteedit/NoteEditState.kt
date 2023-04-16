package com.omtorney.doer.notes.presentation.noteedit

data class NoteEditState(
    val id: Long? = null,
    val text: List<String> = emptyList(),
    val priority: Int = 4,
    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)