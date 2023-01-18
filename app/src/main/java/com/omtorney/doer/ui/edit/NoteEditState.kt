package com.omtorney.doer.ui.edit

data class NoteEditState(
    val id: Int? = null,
    val text: String = "",
    val priority: Int = 4,
    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)