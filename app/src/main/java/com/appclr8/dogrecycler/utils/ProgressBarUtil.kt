package com.appclr8.dogrecycler.utils

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog

class ProgressBarUtil {
    companion object {
        private val TAG = ProgressBarUtil::class.java.name
        private var progressDialog: LottieAlertDialog? = null
        private var progressDialogBuilder: LottieAlertDialog.Builder? = null
        private var countTimer: CountDownTimer? = null

        fun showProgressDialog(
            context: Context,
            title: String,
            message: String,
            cancelable: Boolean) {
            Log.d(TAG, "showProgressDialog")
            try {
                if (progressDialog==null || (progressDialog != null && !progressDialog!!.isShowing)) {
                    Log.d(TAG, "Valid")
                    progressDialog = null
                    progressDialogBuilder = LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
                        .setTitle(title)
                        .setDescription(message)
                        //.setNoneTextColor()

                    progressDialog = progressDialogBuilder!!.build()
                    progressDialog!!.setCancelable(cancelable)
                    progressDialog!!.show()
                }
            } catch (var5: Exception) {
                Log.d(TAG, "Exception: ${var5.message}")
                var5.printStackTrace()
            }
        }

        fun updateText(message: String) {
            if (progressDialog != null && progressDialog!!.isShowing) {
               // progressDialog!!.setTitle(title)
                progressDialogBuilder?.setDescription(message)
                progressDialog?.changeDialog(progressDialogBuilder!!)
            }
        }

        fun dismissProgressDialog() {
            Log.d(TAG, "dismissProgressDialog")
            try {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                    progressDialog = null
                    if (countTimer != null) {
                        countTimer!!.cancel()
                        countTimer = null
                    }
                }
            } catch (var1: Exception) {
                var1.printStackTrace()
            }
        }

        val isShowing: Boolean
            get() = progressDialog != null && progressDialog!!.isShowing
    }
}