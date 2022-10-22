package com.imanancin.storyapp1.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.data.local.database.StoryDatabase
import com.imanancin.storyapp1.data.remote.ApiService
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var dataRepository: DataRepository
    @Mock private lateinit var userPreferences: UserPreferences
    @Mock private lateinit var apiService: ApiService
    @Mock private lateinit var storyDatabase: StoryDatabase

    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val name = "iman"
    private val email = "imannn@gmail.com"
    private val password = "111111"

    @Before
    fun setUp() {
        dataRepository = DataRepository(apiService, userPreferences, storyDatabase)
    }


    @Test fun `when Login success`() = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponse(false)
        Mockito.`when`(apiService.doLogin(email, password)).thenReturn(expectedResponse)
        val result = apiService.doLogin(email, password)
        Mockito.verify(apiService).doLogin(email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertFalse(it) }
    }

    @Test fun `when login failed` () = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponse(true)
        Mockito.`when`(apiService.doLogin(email, password)).thenReturn(expectedResponse)
        val result = apiService.doLogin(email, password)
        Mockito.verify(apiService).doLogin(email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertTrue(it) }
    }

    @Test fun `when register success`() = runTest {
        val expectedResponse = DataDummy.generateDummyCommonResponse(false)
        Mockito.`when`(apiService.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = apiService.doRegister(name, email, password)
        Mockito.verify(apiService).doRegister(name, email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertFalse(it) }
    }

    @Test fun `when register failed`() = runTest {
        val expectedResponse = DataDummy.generateDummyCommonResponse(true)
        Mockito.`when`(apiService.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = apiService.doRegister(name, email, password)
        Mockito.verify(apiService).doRegister(name, email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertTrue(it) }
    }

    @Test fun `add story success`() = runTest {
        val expectedResponse = DataDummy.generateDummyCommonResponse(false)
        Mockito.`when`(apiService.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = apiService.doRegister(name, email, password)
        Mockito.verify(apiService).doRegister(name, email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertFalse(it) }
    }

    @Test fun `add story failed`() = runTest {
        val expectedResponse = DataDummy.generateDummyCommonResponse(true)
        Mockito.`when`(apiService.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = apiService.doRegister(name, email, password)
        Mockito.verify(apiService).doRegister(name, email, password)
        Assert.assertNotNull(result)
        result.error?.let { Assert.assertTrue(it) }
    }

}