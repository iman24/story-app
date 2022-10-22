package com.imanancin.storyapp1

import com.google.gson.Gson
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.data.remote.response.LoginResult

object DataDummy {

    fun generateDummyLoginResponse(error: Boolean): LoginResponse {
        return LoginResponse(LoginResult("iman", "xxx", "xxx"), error, "success")
    }

    fun generateDummyCommonResponse(error: Boolean): CommonResponse {
        return CommonResponse(error, "message")
    }

    fun generateDummyStoriesResponse(): MutableList<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryEntity(
                id = i.toString(),
                photoUrl = "photoURl : $i",
                createdAt = "createdAt: $i",
                lon = 3.3,
                lat = 3.3,
                description = "description: $i",
                name = "name: $i"

            )
            items.add(quote)
        }
        return items
    }


    fun loginResponse(error: String): LoginResponse {
        val sampleResponse = """{
                "error": $error,
                "message": "success",
                "loginResult": {
                    "userId": "user-MqA0eYxG-we-Vy3d",
                    "name": "iman",
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU1xQTBlWXhHLXdlLVZ5M2QiLCJpYXQiOjE2NjMyNTQ0NDh9._eflQ-PVgAVCRvjGOvuYdLg4oOoF8f1jERPaYGsr4fs"
                    }
                }""".trimIndent()

        return Gson().fromJson(sampleResponse, LoginResponse::class.java)
    }

    fun registerSuccessResponse(): CommonResponse {
        val sampleResponse = """{
	        "error": false,
	        "message": "User created"
        }""".trimIndent()
        return Gson().fromJson(sampleResponse, CommonResponse::class.java)
    }

    fun registerFailedResponse(): CommonResponse {
        val sampleResponse = """{
	        "error": true,
	        "message": "Email is already taken"
        }""".trimIndent()
        return Gson().fromJson(sampleResponse, CommonResponse::class.java)
    }

    fun addStorySuccessResponse(): CommonResponse {
        val sampleResponse = """{
	        "error": false,
	        "message": "success"
        }""".trimIndent()
        return Gson().fromJson(sampleResponse, CommonResponse::class.java)
    }

    fun addStoryFailedResponse(): CommonResponse {
        val sampleResponse = """{
	        "error": true,
	        "message": "failed"
        }""".trimIndent()
        return Gson().fromJson(sampleResponse, CommonResponse::class.java)
    }


}