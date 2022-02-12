package com.appclr8.dogrecycler.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.R
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.base.BaseStateUI
import com.appclr8.dogrecycler.base.BaseViewModel
import com.appclr8.dogrecycler.models.MultiResponse
import com.appclr8.dogrecycler.network.DogApi
import com.appclr8.dogrecycler.utils.DeviceUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: App
) : BaseViewModel(application), BaseFragment.DialogClickListener {

    companion object {
        const val NETWORK_DIALOG = 1
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
            liveDataEvent.postValue(
                BaseStateUI.CustomDialog(
                    refId = NETWORK_DIALOG,
                    imageId = R.drawable.ic_network_error,
                    listener = WeakReference(this@HomeViewModel),
                    positiveButtonResId = R.string.tryAgain,
                    messageId = R.string.text_network_error_message,
                    titleId = R.string.text_network_error_title,
                )
            )
        } else {
            DogApi
                .create()
                .getImages()
                .enqueue(object : retrofit2.Callback<MultiResponse> {

                @SuppressLint("CheckResult")
                override fun onFailure(call: Call<MultiResponse>?, t: Throwable?) {
                    Timber.e("Emotional Damage")
                    Timber.e(t)
                    shouldLoadNewImages(false)
                }

                @SuppressLint("CheckResult")
                override fun onResponse(
                    call: Call<MultiResponse>,
                    response: retrofit2.Response<MultiResponse>
                ) {
                    if(response.isSuccessful && response.body() != null){

                        val networkObject = response.body() as MultiResponse

                        for (item in networkObject.message){
                            imageUrls.add(item)
                        }
                        shouldLoadNewImages(true)
                    }
                }
            })
        }
    }

    fun navigateToZoomView(selectedItem : Int) {
        liveDataEvent.postValue(
            BaseStateUI.To(
                directions = HomeFragmentDirections.actionHomeFragmentToZoomFragment(url = imageUrls[selectedItem])
            )
        )
    }

    override fun onPosClicked(refId: Int) {
        when (refId) {
            NETWORK_DIALOG -> {
                //try to load again
                load()
            }
        }
    }

    override fun onNegClicked(refId: Int) {
        when (refId) {
            NETWORK_DIALOG -> {

            }
        }
    }
}