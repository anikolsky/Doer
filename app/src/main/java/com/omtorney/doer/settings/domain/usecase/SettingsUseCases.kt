package com.omtorney.doer.settings.domain.usecase

class SettingsUseCases(
    val getAccentColor: GetAccentColor,
    val setAccentColor: SetAccentColor,
    val getLineDivideState: GetLineDivideState,
    val setLineDivideState: SetLineDivideState,
    val setSecondaryColor: SetSecondaryColor,
    val getSecondaryColor: GetSecondaryColor
)