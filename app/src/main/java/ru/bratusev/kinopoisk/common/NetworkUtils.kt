package ru.bratusev.kinopoisk.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

object NetworkUtils {
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }
        val connectionFlag =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        if (!connectionFlag) Toast.makeText(
            context, "Проверьте подключение к сети.\nНекоторые функции недоступны",
            Toast.LENGTH_SHORT
        ).show()
        return connectionFlag
    }
}