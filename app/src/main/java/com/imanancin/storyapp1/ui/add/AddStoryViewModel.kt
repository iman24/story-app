package com.imanancin.storyapp1.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import java.io.File

class AddStoryViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun uploadImage(file: File, desc: String, lat: Double, lon: Double): LiveData<Results<CommonResponse>> {
        return dataRepository.addStory(file, desc, lat, lon)
    }


}