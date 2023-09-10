package com.berkaksoy.weatherapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.berkaksoy.weatherapp.R
import timber.log.Timber

class StarterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkInternetConnectionAndStartApp()
    }

    private fun checkInternetConnectionAndStartApp() {
        if (isOnline(this))
            startActivity(Intent(this, WeatherActivity::class.java))
        else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.no_internet_connection))
            builder.setMessage(getString(R.string.no_internet_msg))
            builder.setPositiveButton(R.string.try_again) { _, _ ->
                checkInternetConnectionAndStartApp()
            }
            builder.setNegativeButton(R.string.close) { _, _ ->
                finishAffinity()
            }
            builder.setCancelable(false)
            builder.show()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Timber.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Timber.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Timber.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}