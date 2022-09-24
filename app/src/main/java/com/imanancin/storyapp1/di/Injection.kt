package com.imanancin.storyapp1.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.ApiConfig
import com.imanancin.storyapp1.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus

private const val USER_PREF = "user_session"

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, USER_PREF)),
            scope = CoroutineScope(Dispatchers.IO) + SupervisorJob(),
            produceFile = { context.preferencesDataStoreFile(USER_PREF) }
        )
        val userPreferences = UserPreferences.getInstance(dataStore)
        val apiConfig = ApiConfig.getApiService()
        return DataRepository.getInstance(apiConfig, userPreferences)
    }
}