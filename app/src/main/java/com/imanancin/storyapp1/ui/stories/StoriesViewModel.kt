package com.imanancin.storyapp1.ui.stories

import androidx.lifecycle.*
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.model.UserSession
import kotlinx.coroutines.launch


class StoriesViewModel(private val dataRepository: DataRepository): ViewModel() {

    private val data: LiveData<Result<DataStoriesResponse>> = dataRepository.getListStory()

    fun getData(): LiveData<Result<DataStoriesResponse>> {
        return data
    }

    fun session(): LiveData<UserSession> = dataRepository.checkSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            dataRepository.doLogout()
        }
    }

}