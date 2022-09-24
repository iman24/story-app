package com.imanancin.storyapp1.data.remote

import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    suspend fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): CommonResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String?,
        @Query("page") page: String? = null,
        @Query("size") size: String? = null,
        @Query("location") location: String? = null
    ): DataStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): CommonResponse
}