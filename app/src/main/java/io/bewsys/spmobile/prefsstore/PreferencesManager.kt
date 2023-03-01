package io.bewsys.spmobile.prefsstore


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import io.bewsys.spmobile.prefsstore.PreferencesManager.PreferenceKeys.IS_LOGGED_IN
import io.bewsys.spmobile.prefsstore.PreferencesManager.PreferenceKeys.USER_NAME
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlinx.coroutines.flow.map



private const val TAG = "PreferencesManager"
const val USER_PREFERENCES_NAME = "user_preferences"

data class UserPreferences(val isLoggedIn: Boolean, val username: String)

class PreferencesManager(context: Context) {
    private val dataStore = context.createDataStore(USER_PREFERENCES_NAME)

    val preferencesFlow =dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)

                emit(emptyPreferences())

            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isLoggedIn =
                preferences[IS_LOGGED_IN] ?: false

            val username = preferences[USER_NAME] ?: ""
            UserPreferences(isLoggedIn, username)
        }

    suspend fun saveUser(username: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = username
        }
    }

    suspend fun setLoggedIn(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = status
        }
    }

    private object PreferenceKeys {
        val USER_NAME = preferencesKey<String>("username")
        val IS_LOGGED_IN = preferencesKey<Boolean>("logged_in")

    }
}