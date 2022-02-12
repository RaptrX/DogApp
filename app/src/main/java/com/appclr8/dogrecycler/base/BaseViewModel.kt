package com.appclr8.dogrecycler.base

import androidx.lifecycle.AndroidViewModel
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.extensions.SingleLiveEvent
import java.lang.ref.WeakReference

abstract class BaseViewModel(application: App) : AndroidViewModel(application) {
    val cntxt = WeakReference(application.baseContext)
    val liveDataEvent = SingleLiveEvent<BaseStateUI>()
    open fun load() {}
    open fun load(id: String) {}
}