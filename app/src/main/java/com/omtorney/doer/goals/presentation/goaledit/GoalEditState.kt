package com.omtorney.doer.goals.presentation.goaledit

import com.omtorney.doer.goals.domain.model.Steps

data class GoalEditState(
    val id: Long? = null,
    val title: String = "",
    val steps: Steps = Steps(items = emptyList()),
//    val priority: Int = 4,
//    val isPinned: Boolean = false,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)