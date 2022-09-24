package com.imanancin.storyapp1.ui.storydetail

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.imanancin.storyapp1.data.remote.response.StoryItem
import com.imanancin.storyapp1.databinding.ActivityStoryDetailBinding


class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setContentView(binding.root)
        setUpUi()
    }

    private fun setUpUi() {
        val data = intent.extras?.getParcelable<StoryItem>(EXTRA_DATA)
        binding.apply {
            data?.apply {
                tvDetailName.text = name
                tvDetailDescription.text = description
                Glide.with(applicationContext)
                    .load(photoUrl)
                    .into(ivDetailPhoto)
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}