package com.imanancin.storyapp1.ui.register



import androidx.lifecycle.ViewModel
import com.imanancin.storyapp1.data.DataRepository



class RegisterViewModel(private val dataRepository: DataRepository): ViewModel() {


    fun doRegister(
        name: String,
        email: String,
        password: String) = dataRepository.doRegister(name, email, password)

}