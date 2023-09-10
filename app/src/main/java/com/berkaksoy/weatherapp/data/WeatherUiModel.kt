package com.berkaksoy.weatherapp.data

data class WeatherUiModel (
    val currentWeatherGifId: Int?,
    val weatherData: WeatherResponse?,
    val forecastData: ForecastResponse?,
    val forecastMap: MutableMap<String,List<Double?>>?
)