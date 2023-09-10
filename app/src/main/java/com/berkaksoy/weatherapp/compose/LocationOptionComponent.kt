package com.berkaksoy.weatherapp.compose

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkaksoy.weatherapp.BuildConfig
import com.berkaksoy.weatherapp.R
import com.berkaksoy.weatherapp.events.WeatherUiAction
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import timber.log.Timber

@Composable
fun LocationOptionComponent(
    handleUiAction: (WeatherUiAction) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.background(colorResource(id = R.color.yellow))
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp, top = 15.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(R.string.welcome_to_weather_app),
                textAlign = TextAlign.Center
            )
        }
        Divider(thickness = 2.dp, color = Color.Black)
        Column(
            Modifier.background(Color.White)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(R.string.choose_an_option_to_continue),
                textAlign = TextAlign.Center
            )
        }
        Divider(thickness = 2.dp, color = Color.Black)
        Image(
            modifier = Modifier.padding(20.dp),
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = ""
        )
        MapsComponent(handleUiAction = handleUiAction)
        CurrentLocationComponent(handleUiAction = handleUiAction)
    }
}

@Composable
fun MapsComponent(
    handleUiAction: (WeatherUiAction) -> Unit
) {
    val context = LocalContext.current

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    Timber.i("MAP_ACTIVITY", "Place: ${place.name}, ${place.id}")
                    handleUiAction(
                        WeatherUiAction.WeatherDataClicked(
                            lat = place.latLng?.latitude ?: 0.0,
                            long = place.latLng?.longitude ?: 0.0
                        )
                    )
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                it.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    Timber.i("MAP_ACTIVITY", "Place: ${place.name}, ${place.id}")
                }
            }

            Activity.RESULT_CANCELED -> {}
        }
    }

    val launchMapInputOverlay = {
        Places.initialize(context, BuildConfig.API_KEY)
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(context)
        intentLauncher.launch(intent)
    }

    Column {
        Button(
            onClick = launchMapInputOverlay,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.yellow),
                contentColor = Color.Black
            )
        ) {
            Text(stringResource(R.string.select_location))
        }
    }
}


@Composable
fun CurrentLocationComponent(
    handleUiAction: (WeatherUiAction) -> Unit
) {
    Column {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.yellow),
                contentColor = Color.Black
            ),
            onClick = {
                handleUiAction(
                    WeatherUiAction.GetCurrentLocation("")
                )
            }
        ) {
            Text(stringResource(R.string.use_current_location))
        }
    }
}