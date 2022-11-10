package com.imanancin.storyapp1.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.imanancin.storyapp1.*
import com.imanancin.storyapp1.data.local.database.StoryDatabase
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.ui.stories.StoriesAdapter
import com.imanancin.storyapp1.ui.stories.StoryPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dataRepository: DataRepository
    private lateinit var fakeApiService: FakeApiService
    private lateinit var fakedBService: FakeDatabase

    @Mock private lateinit var userPreferences: UserPreferences
    @Mock private lateinit var storyDatabase: StoryDatabase

    private val name = "iman"
    private val email = "imannn@gmail.com"
    private val password = "111111"
    private val lon = 0.0
    private val lat = 0.0
    private val file = DataDummy.dummyFile()
    private val body = "Body string"

    @Before
    fun setUp() {
        fakeApiService = FakeApiService()
        fakedBService = FakeDatabase()
        dataRepository = DataRepository(fakeApiService, userPreferences, storyDatabase, fakedBService)
    }


    @Test fun `do login`() = runTest {
        val expectedResponse = DataDummy.loginResponse("false")
        val actualResponse = dataRepository.doLogin(email, password)
        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse.value)
            Assert.assertTrue(actualResponse.value is Results.Success)
            Assert.assertFalse(actualResponse.value is Results.Error)
            expectedResponse.error?.let { Assert.assertFalse(it) }
            expectedResponse.message?.let { Assert.assertEquals(it, actualResponse.value?.data?.message) }
        }
    }


    @Test fun `do register`() = runTest {
        val expectedResponse = DataDummy.registerSuccessResponse()
        val actualResponse = dataRepository.doRegister(name, email, password)
        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse.value)
            Assert.assertTrue(actualResponse.value is Results.Success)
            Assert.assertFalse(actualResponse.value is Results.Error)
            expectedResponse.error?.let { Assert.assertFalse(it) }
            expectedResponse.message?.let { Assert.assertEquals(it, actualResponse.value?.data?.message) }
        }
    }


    @Test fun `add story`() = runTest {
        val expectedResponse = DataDummy.generateDummyCommonResponse(false)
        val actualResponse = dataRepository.addStory(file, body, lat, lon)
        actualResponse.observeForTesting {
            Assert.assertNotNull(actualResponse.value)
            Assert.assertTrue(actualResponse.value is Results.Success)
            Assert.assertFalse(actualResponse.value is Results.Error)
            expectedResponse.error?.let { Assert.assertFalse(it) }
            expectedResponse.message?.let { Assert.assertEquals(it, actualResponse.value?.data?.message) }
        }
    }

    @Test
    fun `get Story maps from db`() = runTest {
        val expectedData = DataDummy.generateDummyStoriesDb()
        fakedBService.insertStory(DataDummy.generateDummyStoriesDb())
        val actualData = dataRepository.getListStoryDB()
        actualData.observeForTesting {
            Assert.assertNotNull(actualData)
            Assert.assertEquals(expectedData.size, actualData.value?.size)
        }
    }


    @Test
    fun `get story`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStories)
        val expectedStoryData = MutableLiveData<PagingData<StoryEntity>>()
        expectedStoryData.value = data
        CoroutineScope(Dispatchers.IO).launch {
            val actualStories: PagingData<StoryEntity> =
                dataRepository.getListStory().getOrAwaitValue()
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoriesAdapter.DIFF,
                updateCallback = TestUtils.noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(actualStories)
            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStories, differ.snapshot())
            Assert.assertEquals(dummyStories.size, differ.snapshot().size)
            Assert.assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
        }
    }


}