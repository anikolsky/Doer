package com.omtorney.doer.ui.edit

import com.omtorney.doer.util.NotePriority

data class NoteEditState(
    val id: Int? = null,
    val text: String = "",
    val priority: NotePriority = NotePriority.No,
    val isPinned: Boolean = false
)