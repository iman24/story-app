package com.imanancin.storyapp1.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.local.entity.StoryEntity

class MapsViewModel(
    dataRepository: DataRepository
): ViewModel() {

    val datas: LiveData<List<StoryEntity>> = dataRepository.getListStoryDB()

    fun getData(): LiveData<List<StoryEntity>> {
        return datas
    }

}