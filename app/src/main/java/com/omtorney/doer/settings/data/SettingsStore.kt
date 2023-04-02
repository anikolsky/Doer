package com.omtorney.doer.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.omtorney.doer.core.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val LINE_SEPARATOR_STATE = booleanPreferencesKey("line_separator_state")
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val SECONDARY_COLOR = longPreferencesKey("secondary_color")
    }

    val getLineSeparatorState: Flow<Boolean> = dataStore.data.map { preferences ->
            preferences[LINE_SEPARATOR_STATE] ?: true
        }

    suspend fun setLineSeparatorState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[LINE_SEPARATOR_STATE] = state
        }
    }

    val getAccentColor: Flow<Long> = dataStore.data.map { preferences ->
            preferences[ACCENT_COLOR] ?: Constants.INITIAL_ACCENT_COLOR
        }

    suspend fun setAccentColor(color: Long) {
        dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color
        }
    }

    val getSecondaryColor: Flow<Long> = dataStore.data.map { preferences ->
            preferences[SECONDARY_COLOR] ?: Constants.INITIAL_SECONDARY_COLOR
        }

    suspend fun setSecondaryColor(color: Long) {
        dataStore.edit { preferences ->
            preferences[SECONDARY_COLOR] = color
        }
    }
}