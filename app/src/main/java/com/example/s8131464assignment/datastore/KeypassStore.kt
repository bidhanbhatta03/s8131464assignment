package com.example.s8131464assignment.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

class KeypassStore(private val context: Context) {

    private val KEY_KEYPASS: Preferences.Key<String> = stringPreferencesKey("keypass")

    val keypassFlow: Flow<String?> = context.appDataStore.data.map { prefs ->
        prefs[KEY_KEYPASS]
    }

    suspend fun saveKeypass(keypass: String) {
        context.appDataStore.edit { prefs ->
            prefs[KEY_KEYPASS] = keypass
        }
    }
}


