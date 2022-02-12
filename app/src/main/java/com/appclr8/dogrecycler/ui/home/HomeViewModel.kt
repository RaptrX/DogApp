package com.appclr8.dogrecycler.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.base.BaseStateUI
import com.appclr8.dogrecycler.base.BaseViewModel
import com.appclr8.dogrecycler.utils.DeviceUtil
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: App
) : BaseViewModel(application), BaseFragment.DialogClickListener {

    companion object {
        const val OPTIONS_DIALOG = 1
    }

    val imageUrls: ArrayList<String> = ArrayList()
    private val _loadNewImages = MutableLiveData<Boolean>().apply {
        value = false
    }
    val loadNewImages : LiveData<Boolean> = _loadNewImages
    fun shouldLoadNewImages(value : Boolean) {
        _loadNewImages.value = value
    }

    @SuppressLint("CheckResult")
    override fun load() {
        if (!DeviceUtil.isInternetAvailable(cntxt.get()!!)) {

        } else {

        }
    }

    fun showDialog(itemID : Int) {
        liveDataEvent.postValue(
        )
    }

    override fun onPosClicked(refId: Int) {
        //Share Button
        when (refId) {
            OPTIONS_DIALOG -> {

            }
        }
    }

    override fun onNegClicked(refId: Int) {
        //Save Button
        when (refId) {
            OPTIONS_DIALOG -> {

            }
        }
    }
}