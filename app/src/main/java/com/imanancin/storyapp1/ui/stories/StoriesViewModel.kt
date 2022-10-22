package com.imanancin.storyapp1.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import kotlinx.coroutines.launch


class StoriesViewModel(private val dataRepository: DataRepository): ViewModel() {

    private val data: LiveData<PagingData<StoryEntity>> = dataRepository.getListStory().cachedIn(viewModelScope)

    fun getData(): LiveData<PagingData<StoryEntity>> {
        return data
    }

    fun logout() {
        viewModelScope.launch {
            dataRepository.doLogout()
        }
    }

}