package com.omtorney.doer.presentation.screen.settings

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.omtorney.doer.data.remote.database.FirestoreUser

sealed class SettingsEvent {
    data class SetNoteSeparatorState(val state: Boolean) : SettingsEvent()
    data class SetNoteSeparatorSize(val size: Int) : SettingsEvent()
    data class SetAccentColor(val color: Color) : SettingsEvent()
    data class SetSecondaryColor(val color: Color) : SettingsEvent()
    data class CreateUser(val user: FirestoreUser, val context: Context) : SettingsEvent()
    data class UpdateUser(val user: FirestoreUser, val context: Context) : SettingsEvent()
    data class DeleteUser(val user: FirestoreUser, val context: Context) : SettingsEvent()
}
