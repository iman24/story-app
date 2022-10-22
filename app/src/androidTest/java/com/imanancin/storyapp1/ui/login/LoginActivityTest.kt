package com.imanancin.storyapp1.ui.login

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.filters.LargeTest
import com.imanancin.storyapp1.ui.register.RegisterActivity
import com.imanancin.storyapp1.utils.EspressoIdlingResource
import org.junit.Assert.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.intent.IntentCallback
import androidx.test.runner.intent.IntentMonitorRegistry
import com.google.gson.Gson
import com.imanancin.storyapp1.JsonConverter
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.data.remote.response.CommonResponse
import com.imanancin.storyapp1.data.remote.response.DataStoriesResponse
import com.imanancin.storyapp1.di.Injection
import com.imanancin.storyapp1.ui.add.AddStoryActivity
import com.imanancin.storyapp1.ui.maps.MapsActivity
import com.imanancin.storyapp1.ui.stories.StoriesActivity
import com.imanancin.storyapp1.utils.Helper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


@LargeTest
class LoginActivityTest {

    private val mockWebServer = MockWebServer()

    private val name = "iman"
    private val email = "imannn@gmail.com"
    private val password = "111111"
    private val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(StoriesActivity::class.java)

    @get:Rule
    var grantPermissions: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

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
    fun test01_Registration() {
        val body = jsonMockResponse("common.json")
        ActivityScenario.launch(RegisterActivity::class.java)
        onView(withId(R.id.ed_register_name)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_password)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_register_name)).perform(typeText(name), closeSoftKeyboard())
        onView(withId(R.id.ed_register_email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_register_password)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        val res = Gson().fromJson(body, CommonResponse::class.java)
        res.error?.let { assertFalse(it) }
    }

    @Test
    fun test02_Login() {
        val body = jsonMockResponse("login.json")
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        val res = Gson().fromJson(body, CommonResponse::class.java)
        res.error?.let { assertFalse(it) }
    }

    @Test
    fun test03_add_Story_gallery() {
        ActivityScenario.launch(AddStoryActivity::class.java)
        val body = jsonMockResponse("common.json")
        onView(withId(R.id.ivPreview)).check(matches(isDisplayed()))
        onView(withId(R.id.button_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.button_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))

        Intents.init()
        val imgGalleryResult = createImageGallerySetResultStub()
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(imgGalleryResult)
        onView(withId(R.id.button_gallery)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.ed_add_description)).perform(typeText("Test Description"), closeSoftKeyboard())
        onView(withId(R.id.button_add)).perform(click())
        Intents.release()
    }

    @Test
    fun test03_add_Story_Camera() {
        ActivityScenario.launch(AddStoryActivity::class.java)
        val body = jsonMockResponse("common.json")
        onView(withId(R.id.ivPreview)).check(matches(isDisplayed()))
        onView(withId(R.id.button_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.button_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))

        Intents.init()
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        takePhoto(R.id.ivPreview, R.drawable.android_logo)

        Thread.sleep(2000)
        onView(withId(R.id.ed_add_description)).perform(typeText("Test Description"), closeSoftKeyboard())

        Intents.release()
    }

    @Test
    fun test04_list_story() {
        val body = jsonMockResponse("success.json")
        ActivityScenario.launch(StoriesActivity::class.java)
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.btnAdd)).check(matches(isDisplayed()))
        val res = Gson().fromJson(body, DataStoriesResponse::class.java)
        assertNotNull(res)
        res.error?.let { assertFalse(it) }

    }

    @Test
    fun test05_story_details() {
        jsonMockResponse("success.json")
        ActivityScenario.launch(StoriesActivity::class.java)
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))

    }

    @Test
    fun test06_story_maps() {
        val body = jsonMockResponse("success.json")
        ActivityScenario.launch(MapsActivity::class.java)
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        val res = Gson().fromJson(body, DataStoriesResponse::class.java)
        assertNotNull(res)
        res.error?.let { assertFalse(it) }
    }

    private fun jsonMockResponse(json: String): String {
        val body = JsonConverter.readStringFromFile(json)
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(body)
        mockWebServer.enqueue(mockResponse)
        return body
    }


    private fun createImageGallerySetResultStub(): Instrumentation.ActivityResult {
        val resources: Resources = getInstrumentation().context.resources
        val resourceId = R.drawable.android_logo
        val imageUri = Uri.Builder().apply {
            scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            authority(resources.getResourcePackageName(resourceId))
            appendPath(resources.getResourceTypeName(resourceId))
            appendPath(resources.getResourceEntryName(resourceId))
        }.build()
        val resultIntent = Intent()
        resultIntent.data = imageUri
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)
    }

    private fun takePhoto(imageViewId : Int, resourceId : Int) {
        val cameraIntentCallback = intentCallback(resourceId)
        IntentMonitorRegistry.getInstance().addIntentCallback(cameraIntentCallback)
        onView(withId(imageViewId)).perform(click())
//        onView(withId(imageViewId)).check(matches(hasDrawable(imageName)))
        IntentMonitorRegistry.getInstance().removeIntentCallback(cameraIntentCallback)
    }

    private fun intentCallback(resourceId : Int = R.drawable.android_logo) :IntentCallback  {
        return IntentCallback {
            if (it.action == MediaStore.ACTION_IMAGE_CAPTURE) {
                it.extras?.getParcelable<Uri>(MediaStore.EXTRA_OUTPUT).run {
//                    val imageName = File(it.getParcelableExtra<Parcelable>(MediaStore.EXTRA_OUTPUT).toString()).name
                    val context : Context = getInstrumentation().targetContext
                    val outStream = this?.let { it1 -> context.contentResolver.openOutputStream(it1) }
                    val bitmap : Bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                }
            }
        }
    }







}