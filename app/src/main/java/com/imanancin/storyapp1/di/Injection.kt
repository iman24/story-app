package com.imanancin.storyapp1.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.imanancin.storyapp1.BuildConfig
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.UserPreferences
import com.imanancin.storyapp1.data.local.database.StoryDao
import com.imanancin.storyapp1.data.local.database.StoryDatabase
import com.imanancin.storyapp1.data.remote.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val USER_PREF = "user_session"


class Injection {

    companion object {

        var BASE_URL_MOCK: String? = null

        fun provideApiService(context: Context): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                )
            val interceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                runBlocking {
                    val token = provideUserPreferences(context).getUserSession().first().token
                    if (token != "") {
                        val request = originalRequest.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "Bearer $token")
                            .method(originalRequest.method, originalRequest.body)
                            .build()
                        chain.proceed(request)
                    } else {
                        chain.proceed(originalRequest)
                    }
                }

            }
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_MOCK ?: BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(interceptor)
                        .build()
                )
                .build()
            return retrofit.create(ApiService::class.java)
        }

        private fun provideStoryDatabase(context: Context): StoryDatabase {
            return StoryDatabase.getDatabase(context)
        }

        private fun provideStoryDatabaseDao(context: Context): StoryDao {
            return StoryDatabase.getDatabase(context).storyDao()
        }


        fun provideUserPreferences(context: Context): UserPreferences {
            val dataStore = PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(SharedPreferencesMigration(context, USER_PREF)),
                scope = CoroutineScope(Dispatchers.IO) + SupervisorJob(),
                produceFile = { context.preferencesDataStoreFile(USER_PREF) }
            )
            return UserPreferences.getInstance(dataStore)
        }

        fun provideRepository(context: Context): DataRepository {
            return DataRepository.getInstance(
                provideApiService(context),
                provideUserPreferences(context),
                provideStoryDatabase(context),
                provideStoryDatabaseDao(context)
            )
        }

    }
}