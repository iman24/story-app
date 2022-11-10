package com.imanancin.storyapp1

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import java.io.File
import java.lang.reflect.Type

object DataDummy {


    fun generateDummyCommonResponse(boolean: Boolean): CommonResponse {
        return CommonResponse(boolean, "message")
    }

    fun generateDummyStories(): MutableList<StoryEntity> {
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


    fun generateDummyStoriesDb(): List<StoryEntity> {
        val test = """ [
		{
			"id": "story-bGRrPsiYz4ICVbmb",
			"name": "reviewer123",
			"description": "sdfsdfswqeqweqw",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667704671231_sMEA3fGK.jpeg",
			"createdAt": "2022-11-06T03:17:51.232Z",
			"lat": 37.4220936,
			"lon": -122.083922
		},
		{
			"id": "story-oyggnuc1RE9e3dSw",
			"name": "reviewer123",
			"description": "dsfsdfsdfsdfsdf",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667704660384_W2TwRQFs.jpg",
			"createdAt": "2022-11-06T03:17:40.386Z",
			"lat": 37.4220936,
			"lon": -122.083922
		},
		{
			"id": "story-qEElHhGyAwxzpjky",
			"name": "Hasyim",
			"description": "Test",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667703166449_y_gTdbzI.jpg",
			"createdAt": "2022-11-06T02:52:46.451Z",
			"lat": -6.9174633,
			"lon": 107.6191217
		},
		{
			"id": "story-UBIKWaWMKv-JYMce",
			"name": "mut",
			"description": "yduus",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667699215803_XApWhHwZ.jpg",
			"createdAt": "2022-11-06T01:46:55.806Z",
			"lat": -3.6048011,
			"lon": 104.3841013
		},
		{
			"id": "story-OaPEMJIHiT7pggk-",
			"name": "reviewer123",
			"description": "ewrwrwerwe",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667698290876_Sa4FJjop.jpg",
			"createdAt": "2022-11-06T01:31:30.877Z",
			"lat": -7.943740589870029,
			"lon": 113.64108614623547
		},
		{
			"id": "story-D9bcHUXTZthDNyCp",
			"name": "mut",
			"description": "guu",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667697859600_QyMeLow3.jpg",
			"createdAt": "2022-11-06T01:24:19.602Z",
			"lat": -3.6048011,
			"lon": 104.3841013
		},
		{
			"id": "story-fZJGd1m1kW9EjEZ6",
			"name": "Naruto3",
			"description": "tes lokasi 3",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667696606366_469ybNWD.jpg",
			"createdAt": "2022-11-06T01:03:26.367Z",
			"lat": 37.4219983,
			"lon": -122.084
		},
		{
			"id": "story-jGEz_edkgRD2DO6J",
			"name": "Naruto3",
			"description": "tes lokasi lagi",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667695418111_JuwWDVGq.jpg",
			"createdAt": "2022-11-06T00:43:38.114Z",
			"lat": 38.33,
			"lon": 32.33
		},
		{
			"id": "story-CWKKYBJbUKZQc-JQ",
			"name": "Naruto3",
			"description": "tes lokasi",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667695031450_8e2tBhgf.jpg",
			"createdAt": "2022-11-06T00:37:11.454Z",
			"lat": 38.33,
			"lon": 32.33
		},
		{
			"id": "story-kKIZOo0CdgOihM4k",
			"name": "mut",
			"description": "gigy6",
			"photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1667694235134_t9eNIqXT.jpg",
			"createdAt": "2022-11-06T00:23:55.136Z",
			"lat": -3.6048011,
			"lon": 104.3841013
		}
	]""".trimIndent()
        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(test, listStory)

    }

    fun generateDummyStoriesResponses(): DataStoriesResponse {
        val stringJson = """{
                "error": false,
                "message": "Stories fetched successfully",
                "listStory": [
                    {
                        "id": "story-FdZGqDN_mfrIA-da",
                        "name": "debugk",
                        "description": "too much memory leaks ",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666759719665_IffUM61G.jpg",
                        "createdAt": "2022-10-26T04:48:39.667Z",
                        "lat": -6.292085079087383,
                        "lon": 106.7563806474209
                    },
                    {
                        "id": "story-M6JtdwjC-ps2JKJa",
                        "name": "debugk",
                        "description": "Tidak ada deskripsi",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666758794288_MBPfN_da.jpg",
                        "createdAt": "2022-10-26T04:33:14.289Z",
                        "lat": -6.7436798342564455,
                        "lon": 107.8012489154935
                    },
                    {
                        "id": "story-5vDTvXwiimxg87zq",
                        "name": "fathurrahman kurniawan ikhsan",
                        "description": "petani modern",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666755869849_fyeOa1Jg.jpg",
                        "createdAt": "2022-10-26T03:44:29.851Z",
                        "lat": -5.351351351351351,
                        "lon": 105.233433523838
                    },
                    {
                        "id": "story-B8zG3Gnbhjo2PzVe",
                        "name": "fathurrahman kurniawan ikhsan",
                        "description": "belajar untuk menjadi seorang yang tidak bisa tidak",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666755480783_8lXFBsRk.jpg",
                        "createdAt": "2022-10-26T03:38:00.787Z",
                        "lat": -5.351351351351351,
                        "lon": 105.233433523838
                    },
                    {
                        "id": "story-VSdcViRXyj0WfNxv",
                        "name": "Muhammad Rizky",
                        "description": "how are you bro",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666749203220_rB1BBBWk.jpg",
                        "createdAt": "2022-10-26T01:53:23.221Z",
                        "lat": 37.421997,
                        "lon": -122.084
                    },
                    {
                        "id": "story-GtIzP2Bl9lggSTvg",
                        "name": "Muhammad Rizky",
                        "description": "this is my location",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666748307218_Epj-d6Ec.jpg",
                        "createdAt": "2022-10-26T01:38:27.219Z",
                        "lat": 37.421997,
                        "lon": -122.084
                    },
                    {
                        "id": "story-IDiyVAzWlrMXrK2W",
                        "name": "TEST123",
                        "description": "melanggar",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666747761322_hhGDq1Vq.jpg",
                        "createdAt": "2022-10-26T01:29:21.323Z",
                        "lat": -5.99899938,
                        "lon": 106.05848103
                    },
                    {
                        "id": "story-zmF_zvL-AiNpuQOl",
                        "name": "irsyad123",
                        "description": "atap",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666740808861_LQOdE_Z5.jpg",
                        "createdAt": "2022-10-25T23:33:28.862Z",
                        "lat": -6.5968904,
                        "lon": 106.7724727
                    },
                    {
                        "id": "story-InNMI2nNLIrRXabC",
                        "name": "irsyad123",
                        "description": "gvgyyg",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666740640225_T6lSu1Uc.jpg",
                        "createdAt": "2022-10-25T23:30:40.226Z",
                        "lat": -6.594594594594595,
                        "lon": 106.7785612541817
                    },
                    {
                        "id": "story-vaLOp87djG-6Z0mR",
                        "name": "irsyad123",
                        "description": "s2",
                        "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1666740512457_oxuDkoD4.jpg",
                        "createdAt": "2022-10-25T23:28:32.460Z",
                        "lat": -6.594594594594595,
                        "lon": 106.7785612541817
                    }
                ]
            }""".trimIndent()
        return Gson().fromJson(stringJson, DataStoriesResponse::class.java)
    }

    fun dummyFile() = File("test")


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



}