package com.example.wordsapp.data

import android.content.*
import androidx.datastore.core.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import java.io.*

private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LAYOUT_PREFERENCES_NAME)

class SettingsDataStore(context: Context) {
	private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("is_linear_layout_manager")
	
	val preferenceFlow: Flow<Boolean> = context.dataStore.data.catch {
		if (it is IOException) {
			it.printStackTrace()
			emit(emptyPreferences())
		} else {
			throw it
		}
	}.map {
		// On the first run of the app, we will use LinearLayoutManager by default
		it[IS_LINEAR_LAYOUT_MANAGER] ?: true
	}
	
	suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean, context: Context) {
		context.dataStore.edit { it[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager }
	}
}
