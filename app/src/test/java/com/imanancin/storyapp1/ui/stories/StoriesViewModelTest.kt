package com.imanancin.storyapp1.ui.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.imanancin.storyapp1.DataDummy
import com.imanancin.storyapp1.MainDispatcherRule
import com.imanancin.storyapp1.TestUtils.noopListUpdateCallback
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.local.entity.StoryEntity
import com.imanancin.storyapp1.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var dataRepository: DataRepository


    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val dummyStories = DataDummy.generateDummyStories()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStories)
        val expectedQuote = MutableLiveData<PagingData<StoryEntity>>()
        expectedQuote.value = data
        `when`(dataRepository.getListStory()).thenReturn(expectedQuote)

        val mainViewModel = StoriesViewModel(dataRepository)
        val actualStories: PagingData<StoryEntity> = mainViewModel.getData().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories, differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}