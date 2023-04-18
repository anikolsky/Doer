package com.omtorney.doer.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.omtorney.doer.core.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val NOTE_SEPARATOR_STATE = booleanPreferencesKey("note_separator_state")
        val NOTE_SEPARATOR_SIZE = intPreferencesKey("note_separator_size")
        val ACCENT_COLOR = longPreferencesKey("accent_color")
        val SECONDARY_COLOR = longPreferencesKey("secondary_color")
    }

    val getNoteSeparatorState: Flow<Boolean> = dataStore.data.map { preferences ->
            preferences[NOTE_SEPARATOR_STATE] ?: true
        }

    suspend fun setNoteSeparatorState(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTE_SEPARATOR_STATE] = state
        }
    }

    val getNoteSeparatorSize: Flow<Int> = dataStore.data.map { preferences ->
        preferences[NOTE_SEPARATOR_SIZE] ?: 8
    }

    suspend fun setNoteSeparatorSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[NOTE_SEPARATOR_SIZE] = size
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