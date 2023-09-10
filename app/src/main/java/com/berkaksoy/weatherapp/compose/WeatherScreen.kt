package com.berkaksoy.weatherapp.compose

import android.app.Activity
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.berkaksoy.weatherapp.R
import com.berkaksoy.weatherapp.data.WeatherResponse
import com.berkaksoy.weatherapp.events.WeatherUiAction
import com.berkaksoy.weatherapp.events.WeatherUiState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import timber.log.Timber

@Composable
fun WeatherScreen() {
    val viewModel = LocalWeatherViewModel.current
    val state by viewModel.stateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.blue))
    ) {
        when (val currentState = state) {
            is WeatherUiState.Uninitialized -> LocationOptionComponent(
                handleUiAction = viewModel::dispatch
            )

            is WeatherUiState.WeatherDataLoaded -> WeatherComponent(
                weatherData = currentState.uiModel?.weatherData,
                forecastMap = currentState.uiModel?.forecastMap,
                gifId = currentState.uiModel?.currentWeatherGifId,
                handleUiAction = viewModel::dispatch
            )

            is WeatherUiState.Loading -> LoadingView()
        }
    }
}