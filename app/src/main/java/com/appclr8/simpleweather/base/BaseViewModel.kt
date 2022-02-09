package com.appclr8.simpleweather.base

import androidx.lifecycle.AndroidViewModel
import com.appclr8.simpleweather.App
import com.appclr8.simpleweather.extensions.SingleLiveEvent
import java.lang.ref.WeakReference

abstract class BaseViewModel(application: App) : AndroidViewModel(application) {
    val cntxt = WeakReference(application.baseContext)
    val liveDataEvent = SingleLiveEvent<BaseStateUI>()
    open fun load() {}
    open fun load(id: String) {}
}