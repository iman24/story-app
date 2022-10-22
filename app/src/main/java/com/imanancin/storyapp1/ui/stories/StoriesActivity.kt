package com.imanancin.storyapp1.ui.stories



import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.test.core.app.ActivityScenario.launch
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.data.UserPreferences
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.data.remote.response.Stories
import com.imanancin.storyapp1.databinding.ActivityStoriesBinding
import com.imanancin.storyapp1.databinding.ItemStoriesBinding
import com.imanancin.storyapp1.di.Injection
import com.imanancin.storyapp1.ui.add.AddStoryActivity
import com.imanancin.storyapp1.ui.login.LoginActivity
import com.imanancin.storyapp1.ui.maps.MapsActivity
import com.imanancin.storyapp1.ui.storydetail.StoryDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


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
            override fun onClick(data: StoryEntity?, view: ItemStoriesBinding) {
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
            adapter = storiesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storiesAdapter.retry()
                }
            )
        }
    }

    private fun viewModel() {
        lifecycleScope.launch {
            val userPreferences = Injection.provideUserPreferences(this@StoriesActivity).getUserSession().first()
            if (userPreferences.token == "") {
                Intent(activity, LoginActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                }
            }
        }

        storiesAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && storiesAdapter.itemCount < 1) {
                binding.rvStories.isVisible = false
                binding.noData.isVisible = true
            } else {
                binding.rvStories.isVisible = true
                binding.noData.isVisible = false
            }
        }

        viewModel.getData().observe(this) { result ->
            storiesAdapter.submitData(lifecycle, result)
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
            R.id.btn_location -> {
                startActivity(Intent(activity, MapsActivity::class.java))
                return true
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