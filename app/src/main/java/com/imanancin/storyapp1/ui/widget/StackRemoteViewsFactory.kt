package com.imanancin.storyapp1.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.data.remote.ApiService
import com.imanancin.storyapp1.data.remote.response.Stories
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

internal class StackRemoteViewsFactory(
    private val mContext: Context,
    private val apiService: ApiService
) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Stories>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        val identityToken = Binder.clearCallingIdentity()
        coroutineScope.launch {

            try {
                storyWidget().collectLatest { _list ->
                    mWidgetItems.addAll(_list)
                }
            }catch (e:Exception){
                Log.d("Widget", e.printStackTrace().toString() )
            }
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    fun storyWidget(): Flow<List<Stories>> = flow {
        try {
            val response = apiService.getAllStories()
            val data = response.listStory.map { story ->
                with(story) {
                    Stories(
                        photoUrl = photoUrl,
                        createdAt = createdAt,
                        name = name,
                        description = description,
                        lon = lon,
                        id = id,
                        lat = lat)
                }
            }
            emit(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    override fun onDataSetChanged() {
        mWidgetItems.addAll(runBlocking { storyWidget().first() })
    }

    override fun onDestroy() {
        mWidgetItems.clear()
        job.cancel()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(p0: Int): RemoteViews {

        val bitmap = try {
            Glide.with(mContext).asBitmap().load(mWidgetItems[p0].photoUrl).submit().get()
        }catch (e:Exception){
            BitmapFactory.decodeResource(mContext.resources,R.drawable.broken)
        }



        val rv = RemoteViews(mContext.packageName, R.layout.story_app_widget)
        rv.setImageViewBitmap(R.id.image_widget, bitmap)
        val extras = bundleOf(
            StoryAppWidget.EXTRA_ITEM to p0
        )



        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}