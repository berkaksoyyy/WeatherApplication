package com.berkaksoy.weatherapp.repository

import com.berkaksoy.weatherapp.service.WeatherWebService
import com.berkaksoy.weatherapp.data.ForecastResponse
import com.berkaksoy.weatherapp.data.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherWebService: WeatherWebService
) {
    suspend fun getWeather(lat: Double, long: Double): WeatherResponse? =
        weatherWebService.getWeatherInfo(lat = lat,long = long).body()

    suspend fun getForecast(lat: Double, long: Double): ForecastResponse? =
        weatherWebService.getForecast(lat = lat,long = long).body()
}

