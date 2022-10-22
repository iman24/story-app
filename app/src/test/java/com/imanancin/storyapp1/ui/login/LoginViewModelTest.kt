package com.imanancin.storyapp1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.getOrAwaitValue
import org.junit.Assert
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var dataRepository: DataRepository
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule val mainDispatcherRules = MainDispatcherRule()

    val email = "iman@gmail.com"
    val password = "111111"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(dataRepository)
    }


    @Test
    fun `when login successfully`() = runTest {

        val expectedResponse = MutableLiveData<Results<LoginResponse>>()
        expectedResponse.value = Results.Success(DataDummy.loginResponse("false"))
        Mockito.`when`(dataRepository.doLogin(email, password)).thenReturn(expectedResponse)
        val result = loginViewModel.authenticateLogin(email, password).getOrAwaitValue()
        Mockito.verify(dataRepository).doLogin(email, password)
        Assert.assertNotNull(result.data)
        Assert.assertTrue(result is Results.Success)
        Assert.assertFalse(result is Results.Error)
        result.data?.error?.let { Assert.assertFalse(it) }
        println(result.data.toString())

    }

    @Test
    fun `when login failed`() = runTest {

        val expectedResponse = MutableLiveData<Results<LoginResponse>>()
        expectedResponse.value = Results.Success(DataDummy.loginResponse("true"))
        Mockito.`when`(dataRepository.doLogin(email, password)).thenReturn(expectedResponse)
        val result = loginViewModel.authenticateLogin(email, password).getOrAwaitValue()
        Mockito.verify(dataRepository).doLogin(email, password)
        Assert.assertNotNull(result.data)
        Assert.assertTrue(result is Results.Success)
        Assert.assertFalse(result is Results.Error)
        result.data?.error?.let { Assert.assertTrue(it) }
    }
}