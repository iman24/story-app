package com.imanancin.storyapp1.ui.login

import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun authenticateLogin(email: String, password: String) = dataRepository.doLogin(email, password)

}