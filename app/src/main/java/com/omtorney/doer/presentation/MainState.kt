package com.omtorney.doer.presentation

import com.omtorney.doer.util.Constants

data class MainState(
    val accentColor: Long = Constants.INITIAL_ACCENT_COLOR,
    val secondaryColor: Long = Constants.INITIAL_SECONDARY_COLOR,
    val noteSeparatorState: Boolean = true,
    val noteSeparatorSize: Int = 8
)
