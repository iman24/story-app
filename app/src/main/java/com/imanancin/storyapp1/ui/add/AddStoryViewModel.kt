package com.imanancin.storyapp1.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import java.io.File

class AddStoryViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun uploadImage(file: File, desc: String): LiveData<Result<CommonResponse>> {
        return dataRepository.uploadImage(file, desc)
    }


}