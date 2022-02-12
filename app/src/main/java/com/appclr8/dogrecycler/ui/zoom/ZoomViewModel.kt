package com.appclr8.dogrecycler.ui.zoom

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.base.BaseStateUI
import com.appclr8.dogrecycler.base.BaseViewModel
import com.appclr8.dogrecycler.utils.DeviceUtil
import javax.inject.Inject

class ZoomViewModel @Inject constructor(
    application: App
) : BaseViewModel(application) {

    @SuppressLint("CheckResult")
    override fun load() {
        if (!DeviceUtil.isInternetAvailable(cntxt.get()!!)) {

        } else {

        }
    }

    fun navigateToHome() {
        liveDataEvent.postValue(
            BaseStateUI.To(
                directions = ZoomFragmentDirections.actionZoomFragmentToHomeFragment()
            )
        )
    }
}