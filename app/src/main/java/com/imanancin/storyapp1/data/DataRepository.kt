package com.imanancin.storyapp1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.imanancin.storyapp1.data.remote.ApiInterface
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.data.remote.response.StoryItem
import com.imanancin.storyapp1.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DataRepository(
    private val apiConfig: ApiInterface,
    private val userPreferences: UserPreferences
) {

    fun doLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.doLogin(email, password)
            if(response.error == false) {
                userPreferences.saveUserSession(UserSession(
                    response.loginResult?.name,
                    response.loginResult?.token,
                    response.loginResult?.userId
                ))
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }        
    }

    fun doRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<CommonResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.doRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }



    fun getListStory() : LiveData<Result<DataStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.getAllStories("Bearer ${getToken()}")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun storyWidget(): Flow<List<StoryItem>> = flow {
        try {
            val response = apiConfig.getAllStories("Bearer ${getToken()}")
            val data = arrayListOf<StoryItem>()
            response.listStory?.forEach {
                data.add(it)
            }
            emit(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun uploadImage(file: File, description: String): LiveData<Result<CommonResponse>> = liveData {
        emit(Result.Loading)
        try {
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
            val response = apiConfig.uploadImage("Bearer ${getToken()}", imageMultipart, desc)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun checkSession(): Flow<UserSession> = userPreferences.getUserSession()

    suspend fun doLogout() = userPreferences.destroyUserSession()

    private suspend fun getToken() = userPreferences.getUserSession().first().token.toString()

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(apiConfig: ApiInterface, userPreferences: UserPreferences): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiConfig, userPreferences)
            }.also { instance = it }
    }


}