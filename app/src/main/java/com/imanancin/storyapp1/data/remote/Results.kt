package com.imanancin.storyapp1.data.remote

//sealed class Result<out T: Any> {
//    data class Success<out T : Any>(val data: T) : Result<T>()
//    data class Error(val exception: String) : Result<Nothing>()
//    object Loading : Result<Nothing>()
//}
sealed class Results<out T: Any>(val data: T? = null, val message: String? = null) {
    class Success<T : Any>(data: T, message: String? = null) : Results<T>(data, message)
    object Loading : Results<Nothing>()
    class Error<T : Any>(message: String, data: T? = null) : Results<T>(data, message)
}
