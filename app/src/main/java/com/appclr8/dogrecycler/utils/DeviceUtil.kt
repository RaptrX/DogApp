package com.appclr8.dogrecycler.utils

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings

class DeviceUtil {

    companion object {

        fun isAirplaneModeOn(context: Context): Boolean {
            return Settings.Global.getInt(context.contentResolver, "airplane_mode_on", 0) != 0
        }

        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }
}