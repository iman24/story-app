package com.imanancin.storyapp1.ui.stories



import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.GridLayoutManager
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.data.remote.Result
import com.imanancin.storyapp1.data.remote.response.StoryItem
import com.imanancin.storyapp1.databinding.ActivityStoriesBinding
import com.imanancin.storyapp1.databinding.ItemStoriesBinding
import com.imanancin.storyapp1.ui.add.AddStoryActivity
import com.imanancin.storyapp1.ui.login.LoginActivity
import com.imanancin.storyapp1.ui.storydetail.StoryDetailActivity


class StoriesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityStoriesBinding
    private lateinit var activity: StoriesActivity
    private val viewModel: StoriesViewModel by viewModels {
        ViewModelFactory.getInstance(activity)
    }

    private lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.show()
        activity = this

        setUpUi()
        viewModel()
    }

    private fun setUpUi() {
        binding.btnAdd.setOnClickListener(this)
        storiesAdapter = StoriesAdapter()
        storiesAdapter.setOnClickListener(object : StoriesAdapter.OnClickListener {
            override fun onClick(data: StoryItem?, view: ItemStoriesBinding) {
                val optionsCompat: Bundle? =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        Pair(view.ivItemPhoto, "photo"),
                        Pair(view.tvItemName, "name")
                    ).toBundle()
                Intent(activity, StoryDetailActivity::class.java).apply {
                    putExtra(StoryDetailActivity.EXTRA_DATA, data)
                    activity.startActivity(this, optionsCompat)
                }
            }
        })

        binding.rvStories.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = storiesAdapter
        }
    }

    private fun viewModel() {


        viewModel.session().observe(this) { session ->
            if (session.token == "") {
                Intent(activity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                }
            }
        }

        viewModel.getData().observe(this) { result ->
            val progressBar = binding.pbLoading
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        progressBar.visibility = View.GONE
                        if (result.data.listStory?.size == 0) {
                            Toast.makeText(
                                activity,
                                getString(R.string.data_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            storiesAdapter.submitList(result.data.listStory)
                        }

                    }
                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.warning_exit))
                    setMessage(getString(R.string.message_exit))
                    setNegativeButton(R.string.cancel) {_,_ ->  }
                    setPositiveButton(R.string.ok
                    ) { _, _ ->
                        Intent(activity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                        viewModel.logout()
                    }
                }.show()


                true
            }
            R.id.btn_lang -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }

    override fun onClick(p0: View) {
        if(p0.id == R.id.btnAdd) {
            Intent(this, AddStoryActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}