package com.omtorney.doer.goals.presentation.components

import com.omtorney.doer.goals.domain.model.Step

fun calculateProgress(steps: MutableList<Step>): Float {
    return steps.count { it.isAchieved }.toFloat() / steps.size
}