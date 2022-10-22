package com.imanancin.storyapp1.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse

class MapsViewModel(
    dataRepository: DataRepository
): ViewModel() {

    private val data: LiveData<Results<DataStoriesResponse>> = dataRepository.getListStoryMaps()

    fun getData(): LiveData<Results<DataStoriesResponse>> {
        return data
    }

}