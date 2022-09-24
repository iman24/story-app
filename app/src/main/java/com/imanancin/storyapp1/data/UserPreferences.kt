package com.imanancin.storyapp1.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.imanancin.storyapp1.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val name = stringPreferencesKey("name")
    private val userId = stringPreferencesKey("userid")
    private val token = stringPreferencesKey("token")

    fun getUserSession(): Flow<UserSession> {
        return dataStore.data.map { preferences ->
            UserSession(preferences[name] ?: "",
            preferences[token] ?: "",
            preferences[userId] ?: "")
        }
    }

    suspend fun saveUserSession(userSession: UserSession) {
        dataStore.edit { preferences ->
            preferences[name] = userSession.name ?: ""
            preferences[userId] = userSession.userId ?: ""
            preferences[token] = userSession.token ?: ""
        }
    }

    suspend fun destroyUserSession() {
        dataStore.edit { preferences ->
            preferences.remove(name)
            preferences.remove(userId)
            preferences.remove(token)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}