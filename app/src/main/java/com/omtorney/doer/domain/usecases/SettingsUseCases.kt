package com.omtorney.doer.domain.usecases

import com.omtorney.doer.domain.usecases.settings.GetAccentColor
import com.omtorney.doer.domain.usecases.settings.GetNoteSeparatorSize
import com.omtorney.doer.domain.usecases.settings.GetNoteSeparatorState
import com.omtorney.doer.domain.usecases.settings.GetSecondaryColor
import com.omtorney.doer.domain.usecases.settings.SetAccentColor
import com.omtorney.doer.domain.usecases.settings.SetNoteSeparatorSize
import com.omtorney.doer.domain.usecases.settings.SetNoteSeparatorState
import com.omtorney.doer.domain.usecases.settings.SetSecondaryColor

data class SettingsUseCases(
    val getAccentColor: GetAccentColor,
    val setAccentColor: SetAccentColor,
    val getSecondaryColor: GetSecondaryColor,
    val setSecondaryColor: SetSecondaryColor,
    val getNoteSeparatorState: GetNoteSeparatorState,
    val setNoteSeparatorState: SetNoteSeparatorState,
    val getNoteSeparatorSize: GetNoteSeparatorSize,
    val setNoteSeparatorSize: SetNoteSeparatorSize
)
