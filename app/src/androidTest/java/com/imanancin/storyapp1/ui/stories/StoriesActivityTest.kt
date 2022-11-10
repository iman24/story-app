package com.imanancin.storyapp1.ui.stories

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ui.add.AddStoryActivity
import com.imanancin.storyapp1.ui.maps.MapsActivity
import com.imanancin.storyapp1.ui.storydetail.StoryDetailActivity
import com.imanancin.storyapp1.utils.EspressoIdlingResource
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@LargeTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class StoriesActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(StoriesActivity::class.java)


    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test01_get_stories_successfully() {
        onView(withId(R.id.btnAdd)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(7))
    }

    @Test
    fun test02_get_detail_stories() {
        Intents.release()
        Intents.init()
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))
        intended(hasComponent(StoryDetailActivity::class.java.name))
    }

    @Test
    fun test03_show_maps_activity() {
        onView(withId(R.id.btn_location)).perform(click())
        intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    @Test
    fun test04_show_addStory_activity() {
        onView(withId(R.id.btnAdd)).perform(click())
        intended(hasComponent(AddStoryActivity::class.java.name))
        onView(withId(R.id.ivPreview)).check(matches(isDisplayed()))
        onView(withId(R.id.button_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.button_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))
    }



}
