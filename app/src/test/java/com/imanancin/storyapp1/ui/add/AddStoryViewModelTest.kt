package com.imanancin.storyapp1.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var dataRepository: DataRepository
    private lateinit var addStoryViewModel: AddStoryViewModel

    @get:Rule val mainDispatcherRules = MainDispatcherRule()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(dataRepository)
    }


    @Test
    fun `when successfully upload story`() {
        val expectedResponse = MutableLiveData<Results<CommonResponse>>()
        expectedResponse.value = Results.Success(DataDummy.generateDummyCommonResponse(false))
        `when`(dataRepository.addStory(description = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg"))).thenReturn(expectedResponse)
        val result = addStoryViewModel.uploadImage(desc = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg")).getOrAwaitValue()
        verify(dataRepository).addStory(description = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg"))
        assertTrue(result is Results.Success)
        assertFalse(result is Results.Error)
        result.data?.error?.let { assertFalse(it) }
    }

    @Test
    fun `when failed upload story`() {
        val expectedResponse = MutableLiveData<Results<CommonResponse>>()
        expectedResponse.value = Results.Error(data = DataDummy.generateDummyCommonResponse(true), message = "Error")
        `when`(dataRepository.addStory(description = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg"))).thenReturn(expectedResponse)
        val result = addStoryViewModel.uploadImage(desc = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg")).getOrAwaitValue()
        verify(dataRepository).addStory(description = "deskripsi", lat = 0.0, lon = 0.0, file = File("tes.jpg"))
        Assert.assertNotNull(result.data)
        assertTrue(result is Results.Error)
        assertFalse(result is Results.Success)
        result.data?.error?.let { assertTrue(it) }
    }


}
