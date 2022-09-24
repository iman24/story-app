package com.imanancin.storyapp1.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.imanancin.storyapp1.di.Injection

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = StackRemoteViewsFactory(this.applicationContext, Injection.provideRepository(this.applicationContext))
}