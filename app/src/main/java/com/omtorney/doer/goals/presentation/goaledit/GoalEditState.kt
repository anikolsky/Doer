package com.omtorney.doer.goals.presentation.goaledit

import com.omtorney.doer.goals.domain.model.Step

data class GoalEditState(
    val id: Long? = null,
    val title: String = "",
    val steps: MutableList<Step> = mutableListOf(),
    val progress: Float = 0.0f,
    val createdAt: Long = 0L,
    val modifiedAt: Long = 0L
)