package com.imanancin.storyapp1.data

import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.data.remote.ApiService
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {
    override suspend fun doRegister(name: String, email: String, password: String): CommonResponse {
        return DataDummy.registerSuccessResponse()
    }

    override suspend fun doLogin(email: String, password: String): LoginResponse {
        return DataDummy.loginResponse("false")
    }

    override suspend fun getAllStories(location: Int, page: Int?, size: Int?): DataStoriesResponse {
        return  DataDummy.generateDummyStoriesResponses()
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ): CommonResponse {
        return CommonResponse(false, "message")
    }

}