package com.imanancin.storyapp1.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.local.entity.StoryEntity


class StoriesViewModel(dataRepository: DataRepository): ViewModel() {

    private val data: LiveData<PagingData<StoryEntity>> = dataRepository.getListStory().cachedIn(viewModelScope)

    fun getData(): LiveData<PagingData<StoryEntity>> {
        return data
    }

}