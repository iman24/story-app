package com.imanancin.storyapp1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.imanancin.storyapp1.data.local.database.StoryDao
import com.imanancin.storyapp1.data.local.database.StoryDatabase
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.ApiService
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.model.UserSession
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DataRepository(
    private val apiConfig: ApiService,
    private val userPreferences: UserPreferences,
    private val storyDatabase: StoryDatabase,
    private val storyDao: StoryDao,
) {

    fun doLogin(email: String, password: String): LiveData<Results<LoginResponse>> = liveData {

        emit(Results.Loading)
        try {
            val response = apiConfig.doLogin(email, password)
            if(response.error == false) {
                userPreferences.saveUserSession(UserSession(
                    response.loginResult?.name,
                    response.loginResult?.token,
                    response.loginResult?.userId
                ))
                emit(Results.Success(response))
            } else {
                response.message?.let { Results.Error<String>(it) }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }        
    }

    fun doRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Results<CommonResponse>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiConfig.doRegister(name, email, password)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getListStory() : LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiConfig),
            pagingSourceFactory = {
                storyDao.getAllStoryPaging()
            }
        ).liveData

    }

    fun getListStoryDB(): LiveData<List<StoryEntity>> = liveData {
        emitSource(storyDao.getAllStory())
    }


    fun addStory(file: File, description: String, lat: Double, lon: Double): LiveData<Results<CommonResponse>> = liveData {
        emit(Results.Loading)
        try {
            val desc = description.toRequestBody("text/plain".toMediaType())
            val latitude = lat.toFloat()
            val longitude = lon.toFloat()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
            val response = apiConfig.uploadImage(
                file = imageMultipart,
                description =  desc,
                lat = latitude,
                lon = longitude)
            emit(Results.Success(response))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }

    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(apiConfig: ApiService, userPreferences: UserPreferences, storyDatabase: StoryDatabase, storyDao: StoryDao): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiConfig, userPreferences, storyDatabase, storyDao)
            }.also { instance = it }
    }


}