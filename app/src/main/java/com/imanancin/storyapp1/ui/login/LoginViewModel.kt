package com.imanancin.storyapp1.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.LoginResponse

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {


    fun authenticateLogin(email: String, password: String): LiveData<Results<LoginResponse>> {
        return dataRepository.doLogin(email, password)
    }

}