package com.imanancin.storyapp1.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var dataRepository: DataRepository
    private lateinit var mapViewModel: MapsViewModel

    @get:Rule val mainDispatcherRules = MainDispatcherRule()


    @Test
    fun `when get data successfully`() = runTest {
        val expectedResponse = MutableLiveData<List<StoryEntity>>()
        expectedResponse.value = DataDummy.generateDummyStoriesDb()
        Mockito.`when`(dataRepository.getListStoryDB()).thenReturn(expectedResponse)
        mapViewModel = MapsViewModel(dataRepository)
        val result = mapViewModel.getData().getOrAwaitValue()
        Mockito.verify(dataRepository).getListStoryDB()
        Assert.assertNotNull(result)
        Assert.assertEquals(expectedResponse.value?.size, result.size)
    }
}