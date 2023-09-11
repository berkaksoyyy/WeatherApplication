package com.berkaksoy.weatherapp.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import com.berkaksoy.weatherapp.BuildConfig
import com.berkaksoy.weatherapp.R
import com.berkaksoy.weatherapp.events.WeatherEvent
import com.berkaksoy.weatherapp.events.WeatherUiAction
import com.berkaksoy.weatherapp.compose.LocalWeatherViewModel
import com.berkaksoy.weatherapp.compose.WeatherScreen
import com.berkaksoy.weatherapp.extensions.withEvent
import com.berkaksoy.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocationPermissions()
        setContentView(getComposableContent())
        observeEvents()
    }

    override fun onBackPressed() {}

    private fun getComposableContent(): View = ComposeView(context = this).apply {
        setContent {
            CompositionLocalProvider(
                LocalWeatherViewModel provides viewModel
            ) {
                WeatherScreen()
            }
        }
    }

    private fun currentLocation(){
        Places.initialize(this, BuildConfig.API_KEY)
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(fields)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {

            val placesClient = Places.createClient(this)
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    val placeLikelihood = response.placeLikelihoods.maxBy { it.likelihood }
                    viewModel.dispatch(
                        WeatherUiAction.WeatherDataClicked(
                            lat = placeLikelihood?.place?.latLng?.latitude ?: 0.0,
                            long = placeLikelihood?.place?.latLng?.longitude ?: 0.0,
                        )
                    )
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Timber.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            viewModel.dispatch(WeatherUiAction.WeatherDataCleared(""))
            Toast.makeText(
                this,
                getString(R.string.permission_msg),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getLocationPermissions(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION, false
                ) -> {}
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION, false
                ) -> {}
                else -> {}
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun observeEvents() {
        withEvent(viewModel) { event ->
            when(event) {
                is WeatherEvent.GetCurrentLocation -> currentLocation()
            }
        }
    }
}