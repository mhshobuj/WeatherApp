package com.mhs.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
object NetworkChecking {
    /**
     * Gets the connectivity status as a string.
     *
     * @param context The application context.
     * @return A string indicating the connectivity status.
     */
    fun getConnectivityStatusString(context: Context): String {
        var status: String? = null
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled"
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled"
            }
        } else {
            status = "No internet is available"
        }

        return status ?: "Unknown status"
    }
}