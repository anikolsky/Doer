package com.omtorney.doer.presentation.screen.goals.components

import com.omtorney.doer.data.model.goal.Step

fun calculateProgress(steps: MutableList<Step>): Float {
    return steps.count { it.isAchieved }.toFloat() / steps.size
}
