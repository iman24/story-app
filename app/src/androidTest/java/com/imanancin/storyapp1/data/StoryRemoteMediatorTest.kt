package com.imanancin.storyapp1.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.imanancin.storyapp1.data.local.database.StoryDatabase
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.ApiService
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.data.remote.response.Stories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }



    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }

}


class FakeApiService : ApiService {
    override suspend fun doRegister(name: String, email: String, password: String): CommonResponse {
        TODO("Not yet implemented")
    }

    override suspend fun doLogin(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStories(location: Int, page: Int?, size: Int?): DataStoriesResponse {
        val items: MutableList<Stories> = arrayListOf()
        for (i in 0..100) {
            val quote = Stories(
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
        if (page != null && size != null) {
            items.subList((page - 1) * size, (page - 1) * size + size)
        }
        return DataStoriesResponse(listStory = items, false, "Success")
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ): CommonResponse {
        return CommonResponse(false, "")
    }

}