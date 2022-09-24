package com.imanancin.storyapp1.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.data.DataRepository
import com.imanancin.storyapp1.data.remote.response.StoryItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

internal class StackRemoteViewsFactory(
    private val mContext: Context,
    private val dataRepository: DataRepository
) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<StoryItem>()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        val identityToken = Binder.clearCallingIdentity()
        coroutineScope.launch {

            try {
                dataRepository.storyWidget().collectLatest { _list ->
                    mWidgetItems.addAll(_list)
                }
            }catch (e:Exception){
                Log.d("Widget", e.printStackTrace().toString() )
            }
        }
        Binder.restoreCallingIdentity(identityToken)
    }



    override fun onDataSetChanged() {
        mWidgetItems.addAll(runBlocking { dataRepository.storyWidget().first() })
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