package com.omtorney.doer.util

import androidx.compose.ui.graphics.Color

sealed class NotePriority(
    val status: Int,
    val color: Color
) {
    object High : NotePriority(1, Color(Constants.highPriorityColor))
    object Medium : NotePriority(2, Color(Constants.mediumPriorityColor))
    object Low : NotePriority(3, Color(Constants.lowPriorityColor))
    object No : NotePriority(4, Color.Gray)
}