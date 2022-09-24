package com.imanancin.storyapp1

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.di.Injection
import com.imanancin.storyapp1.ui.add.AddStoryViewModel
import com.imanancin.storyapp1.ui.login.LoginViewModel
import com.imanancin.storyapp1.ui.register.RegisterViewModel
import com.imanancin.storyapp1.ui.stories.StoriesViewModel


class ViewModelFactory(
    private val repository: DataRepository
) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            return StoriesViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}