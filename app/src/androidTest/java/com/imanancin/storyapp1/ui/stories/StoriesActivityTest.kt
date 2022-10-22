package com.imanancin.storyapp1.ui.stories

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.imanancin.storyapp1.JsonConverter
import com.imanancin.storyapp1.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.di.Injection


@MediumTest
@RunWith(AndroidJUnit4::class)
class StoriesActivityTest {

    private val mockWebServer = MockWebServer()


    @Before
    fun setUp() {
        mockWebServer.start(8080)
        Injection.BASE_URL_MOCK = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun get_stories_successfully() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success.json"))
        mockWebServer.enqueue(mockResponse)
        ActivityScenario.launch(StoriesActivity::class.java)
        onView(withId(R.id.btnAdd)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
    }

    @Test
    fun get_stories_when_empty() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("empty.json"))
        mockWebServer.enqueue(mockResponse)
        ActivityScenario.launch(StoriesActivity::class.java)
        onView(withId(R.id.no_data)).check(matches(isDisplayed()))
    }



}
