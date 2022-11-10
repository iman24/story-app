package com.imanancin.storyapp1.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.LoginResponse
import com.imanancin.storyapp1.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var dataRepository: DataRepository
    private lateinit var registerViewModel: RegisterViewModel

    @get:Rule val mainDispatcherRules = MainDispatcherRule()

    private val name = "Iman nurjaman"
    private val email = "iman@gmail.com"
    private val password = "111111"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(dataRepository)
    }

    @Test
    fun `when register successfully`() {
        val expectedResponse = MutableLiveData<Results<CommonResponse>>()
        expectedResponse.value = Results.Success(DataDummy.registerSuccessResponse())
        Mockito.`when`(dataRepository.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = registerViewModel.doRegister(name, email, password).getOrAwaitValue()
        Mockito.verify(dataRepository).doRegister(name, email, password)
        assertNotNull(result.data)
        assertTrue(result is Results.Success)
        assertFalse(result is Results.Error)
        result.data?.error?.let { assertFalse(it) }
    }

    @Test
    fun `when register failed or email is already taken`() {
        val expectedResponse = MutableLiveData<Results<CommonResponse>>()
        expectedResponse.value = Results.Error("", CommonResponse())
        Mockito.`when`(dataRepository.doRegister(name, email, password)).thenReturn(expectedResponse)
        val result = registerViewModel.doRegister(name, email, password).getOrAwaitValue()
        Mockito.verify(dataRepository).doRegister(name, email, password)
        assertNotNull(result.data)
        assertTrue(result is Results.Error)
        assertFalse(result is Results.Success)
        result.data?.error?.let { assertTrue(it) }
    }


}