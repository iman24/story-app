package com.imanancin.storyapp1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagingSource
import com.imanancin.storyapp1.data.local.database.StoryDao
import com.imanancin.storyapp1.data.local.entity.StoryEntity

class FakeDatabase: StoryDao {

    private var storydata = mutableListOf<StoryEntity>()

    override suspend fun insertStory(stories: List<StoryEntity>) {
        storydata.addAll(stories)
    }

    override fun getAllStoryPaging(): PagingSource<Int, StoryEntity> {
        return StoryPagingSource(storydata)
    }

    override fun getAllStory(): LiveData<List<StoryEntity>> = liveData {
        emit(storydata)
    }

    override suspend fun deleteAll() {
        storydata.clear()
    }
}