package io.bewsys.spmobile.data.prefsstore


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.*
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.EMAIL
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.ID
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.IS_LOGGED_IN
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.NAME
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.PHONE_NUMBER
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.SUPERVISOR_ID
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.SUPERVISOR_NAME
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.TEAM_LEADER
import io.bewsys.spmobile.data.prefsstore.PreferencesManager.PreferenceKeys.TOKEN
import io.bewsys.spmobile.data.remote.model.login.User

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val TAG = "PreferencesManager"
const val USER_STORE_NAME = "user_preferences"

data class UserPreferences(
    val isLoggedIn: Boolean,
    val name: String,
    val email: String,
    val id: Long?,
    val phoneNumber: String,
    val teamLeader: String,
    val supervisorName: String,
    val supervisorId: String,
    val token: String
)

class PreferencesManager(context: Context) {
    private val dataStore = context.createDataStore(
        USER_STORE_NAME
    )

    val preferencesFlow = dataStore.data
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
            val name = preferences[NAME] ?: ""
            val email = preferences[EMAIL] ?: ""
            val id = preferences[ID]
            val phoneNumber = preferences[PHONE_NUMBER] ?: ""
            val teamLeader = preferences[TEAM_LEADER] ?: ""
            val supervisorName = preferences[SUPERVISOR_NAME] ?: ""
            val supervisorId = preferences[SUPERVISOR_ID] ?: ""
            val token = preferences[TOKEN] ?: ""
            UserPreferences(
                isLoggedIn,
                name,
                email,
                id,
                phoneNumber,
                teamLeader,
                supervisorName,
                supervisorId,
                token
            )
        }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name.toString()
            preferences[EMAIL] = user.email.toString()
            preferences[ID] = user.id as Long
            preferences[PHONE_NUMBER] = user.phone_number.toString()
            preferences[TEAM_LEADER] =
                user.enumerator_team_leader_relation?.team_leader?.name.toString()
            preferences[SUPERVISOR_NAME] =
                user.enumerator_team_leader_relation?.team_leader?.supervisor.toString()
            preferences[SUPERVISOR_ID] =
                user.enumerator_team_leader_relation?.team_leader?.id.toString()
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun setLoggedIn(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = status
        }
    }

    private object PreferenceKeys {
        val TOKEN = preferencesKey<String>("token")
        val NAME = preferencesKey<String>("name")
        val PERMISSIONS = preferencesKey<String>("permissions")
        val EMAIL = preferencesKey<String>("email")
        val ID = preferencesKey<Long>("id")
        val PHONE_NUMBER = preferencesKey<String>("phone_number")
        val TEAM_LEADER = preferencesKey<String>("team_leader")
        val SUPERVISOR_NAME = preferencesKey<String>("supervisor_name")
        val SUPERVISOR_ID = preferencesKey<String>("supervisor_id")
        val IS_LOGGED_IN = preferencesKey<Boolean>("logged_in")
    }
}