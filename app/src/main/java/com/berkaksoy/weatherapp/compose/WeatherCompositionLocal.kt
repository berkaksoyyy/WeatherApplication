package com.berkaksoy.weatherapp.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.berkaksoy.weatherapp.viewmodel.WeatherViewModel

internal val LocalWeatherViewModel: ProvidableCompositionLocal<WeatherViewModel> =
    compositionLocalOf { error("No ViewModel Provided") }