package com.imanancin.storyapp1

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

object Utils {
    fun clickItemOnRecyclerView(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click item"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v = view?.findViewById(id) as ImageView
                v.performClick()
            }

        }
    }
}