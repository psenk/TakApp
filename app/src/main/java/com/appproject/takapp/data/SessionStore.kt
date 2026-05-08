package com.appproject.takapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionStore(private val context: Context) {

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("session_token")
        private val KEY_EXPIRES_AT = longPreferencesKey("session_expires_at")
    }

    val sessionToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_TOKEN]
        }

    val validSessionToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            val token = preferences[KEY_TOKEN]
            val expiresAt = preferences[KEY_EXPIRES_AT] ?: 0L
            val nowInSeconds = System.currentTimeMillis() / 1000

            if (token != null && nowInSeconds < expiresAt) {
                token
            } else {
                null
            }
        }

    suspend fun saveSession(token: String, expiresAt: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
            preferences[KEY_EXPIRES_AT] = expiresAt
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
            preferences.remove(KEY_EXPIRES_AT)
        }
    }
}