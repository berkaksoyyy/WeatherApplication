package com.berkaksoy.weatherapp.usecase

import com.berkaksoy.weatherapp.repository.WeatherRepository
import com.berkaksoy.weatherapp.data.WeatherUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend fun getWeatherData(
        lat: Double,
        long: Double,
    ): WeatherUiModel = withContext(Dispatchers.IO) {
            val getWeatherAsync = async {
                weatherRepository.getWeather(lat,long)
            }
            val getForecastAsync = async {
                weatherRepository.getForecast(lat,long)
            }

            val weather = getWeatherAsync.await()
            val forecast = getForecastAsync.await()

            return@withContext WeatherUiModel(
                currentWeatherGifId = null,
                weatherData = weather,
                forecastData = forecast,
                forecastMap = null,
            )
        }
}