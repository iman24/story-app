package com.imanancin.storyapp1.data.local.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imanancin.storyapp1.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStoryPaging(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM stories")
    fun getAllStory(): LiveData<List<StoryEntity>>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}