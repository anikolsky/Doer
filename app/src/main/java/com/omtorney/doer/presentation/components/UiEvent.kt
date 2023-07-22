package com.omtorney.doer.presentation.components

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Save : UiEvent()
    object HideSnackbar : UiEvent()
}
