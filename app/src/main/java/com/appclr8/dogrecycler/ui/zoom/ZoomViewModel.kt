package com.appclr8.dogrecycler.ui.zoom

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appclr8.dogrecycler.App
import com.appclr8.dogrecycler.GlideApp
import com.appclr8.dogrecycler.R
import com.appclr8.dogrecycler.base.BaseFragment
import com.appclr8.dogrecycler.base.BaseStateUI
import com.appclr8.dogrecycler.base.BaseViewModel
import com.appclr8.dogrecycler.utils.DeviceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.ref.WeakReference
import javax.inject.Inject

class ZoomViewModel @Inject constructor(
    application: App
) : BaseViewModel(application), BaseFragment.DialogClickListener {

    private var _url = MutableLiveData<String>().apply {
        value = ""
    }
    var url : LiveData<String> = _url
    fun setUrl(value : String) {
        _url.value = value
    }

    private var _shareImage = MutableLiveData<Boolean>().apply {
        value = false
    }
    var shareImage : LiveData<Boolean> = _shareImage
    fun shouldShareImage(value : Boolean) {
        _shareImage.value = value
    }

    private var _noNetwork = MutableLiveData<Boolean>().apply {
        value = false
    }
    var noNetwork : LiveData<Boolean> = _noNetwork
    fun shouldShowNoNetwork(value : Boolean) {
        _noNetwork.value = value
    }

    @SuppressLint("CheckResult")
    override fun load() {
        if (!DeviceUtil.isInternetAvailable(cntxt.get()!!)) {
            shouldShowNoNetwork(value = true)
        }
    }

    fun navigateToHome() {
        liveDataEvent.postValue(
            BaseStateUI.To(
                directions = ZoomFragmentDirections.actionZoomFragmentToHomeFragment()
            )
        )
    }

    fun shareImage() {
        shouldShareImage(value = true)
    }

    fun saveImage() {
        var imagePath : String? = ""
        viewModelScope.executeAsyncTask(
            onPreExecute = {
                liveDataEvent.postValue(
                    BaseStateUI.ProgressDialogShow(
                        titleId = R.string.text_network_error_title,
                        messageId = R.string.figuring_out
                    )
            )},
            onPostExecute = {liveDataEvent.value = BaseStateUI.ProgressDialogHide()
                            liveDataEvent.postValue(
                                BaseStateUI.InfoDialog(
                                    messageText = "Your image was saved to :\n${imagePath}",
                                    listener = WeakReference(this@ZoomViewModel),
                                    cancelable = true
                                )
                            )},
            doInBackground = {imagePath = saveImage(
                GlideApp.with(cntxt.get()!!)
                    .asBitmap()
                    .load(url.value) // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get()
            )}
        )
    }

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "${System.currentTimeMillis()}.jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/DOG_APP"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return savedImagePath
    }

    private fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result =
            withContext(Dispatchers.IO) { // runs in background thread without blocking the Main Thread
                doInBackground()
            }
        onPostExecute(result)
    }

    override fun onPosClicked(refId: Int) {
        //Do Nothing
    }

    override fun onNegClicked(refId: Int) {
        //Do Nothing
    }
}