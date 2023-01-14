package com.omtorney.doer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.omtorney.doer.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DoerSettings")
        val LINE_SEPARATOR_STATE = booleanPreferencesKey("line_separator_state")
        val ACCENT_COLOR = longPreferencesKey("accent_color")
    }

    val getLineSeparatorState: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[LINE_SEPARATOR_STATE] ?: true
        }

    suspend fun setLineSeparatorState(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LINE_SEPARATOR_STATE] = state
        }
    }

    val getAccentColor: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[ACCENT_COLOR] ?: Constants.initialColor
        }

    suspend fun setAccentColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[ACCENT_COLOR] = color
        }
    }
}