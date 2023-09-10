package com.berkaksoy.weatherapp.viewmodel

import androidx.lifecycle.viewModelScope
import com.berkaksoy.weatherapp.events.WeatherEvent
import com.berkaksoy.weatherapp.events.WeatherUiAction
import com.berkaksoy.weatherapp.events.WeatherUiState
import com.berkaksoy.weatherapp.data.ForecastResponse
import com.berkaksoy.weatherapp.data.GifIdEnum.*
import com.berkaksoy.weatherapp.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
) : BaseViewModel<WeatherUiState, WeatherEvent>(WeatherUiState.Uninitialized()) {

    private fun handleWeatherData(lat: Double, long: Double) {
        viewModelScope.launch(defaultExceptionHandler) {
            val uiModel = weatherUseCase.getWeatherData(lat, long)

            val gifId = when (uiModel.weatherData?.weather?.firstOrNull()?.main) {
                THUNDERSTORM.code -> THUNDERSTORM.id
                DRIZZLE.code -> DRIZZLE.id
                RAIN.code -> RAIN.id
                SNOW.code -> SNOW.id
                CLOUD.code -> CLOUD.id
                else -> CLEAR.id
            }

            setState {
                WeatherUiState.WeatherDataLoaded(
                    uiModel.copy(
                        currentWeatherGifId = gifId,
                        forecastMap = handleForecastData(uiModel.forecastData)
                    )
                )
            }
        }
    }

    private fun handleForecastData(forecastResponse: ForecastResponse?)
            : MutableMap<String, List<Double?>> {
        val dateMap = forecastResponse?.list?.groupBy { item -> item.dateText?.substring(0, 10) }

        val maxMinMap = mutableMapOf<String, List<Double?>>()
        dateMap?.forEach { forecastMapObject ->
            val maxForTheDay = forecastMapObject.value.maxWith(
                Comparator.comparingDouble { it.main?.tempMax ?: 0.0 }
            ).main?.tempMax
            val minForTheDay = forecastMapObject.value.minWith(
                Comparator.comparingDouble { it.main?.tempMin ?: 0.0 }
            ).main?.tempMin

            maxMinMap[forecastMapObject.key ?: ""] = listOf(maxForTheDay, minForTheDay)
        }
        dateMap?.keys?.let { maxMinMap.remove(it.firstOrNull()) }
        return maxMinMap
    }

    private fun handleClear() {
        setState { WeatherUiState.Uninitialized() }
    }

    private fun getCurrentLocation() {
        setState { WeatherUiState.Loading() }
        pushEvent(WeatherEvent.GetCurrentLocation(""))
    }

    fun dispatch(action: WeatherUiAction) {
        when (action) {
            is WeatherUiAction.WeatherDataClicked -> handleWeatherData(action.lat, action.long)
            is WeatherUiAction.WeatherDataCleared -> handleClear()
            is WeatherUiAction.GetCurrentLocation -> getCurrentLocation()
        }
    }
}